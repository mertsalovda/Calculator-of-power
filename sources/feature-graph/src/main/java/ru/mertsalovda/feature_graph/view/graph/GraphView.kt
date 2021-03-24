package ru.mertsalovda.feature_graph.view.graph

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.graphics.toRectF
import ru.mertsalovda.feature_graph.R
import kotlin.math.max

class GraphView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private companion object {
        const val DEFAULT_SCALE = 0.2f

        const val DEFAULT_GRID_COLOR = Color.GREEN

        const val DEFAULT_AXIS_WIDTH = 6f
        const val DEFAULT_AXIS_COLOR = Color.RED

        const val DEFAULT_GRAPH_WIDTH = 6f
        const val DEFAULT_GRAPH_COLOR = Color.YELLOW

        const val DEFAULT_TEXT_SIZE = 130f
        const val DEFAULT_TEXT_COLOR = Color.BLUE

        const val DURATION = 1000L
    }

    /** Масштаб графика */
    var scale: Float = DEFAULT_SCALE
        private set

    private var gridStep: Int = 0
    private var deltaX: Float = 0f
    private var deltaY: Float = 0f
    private var dX = 0f
    private var dY = 0f

    private val textRect = Rect()

    private val graphAnimator: ValueAnimator
    private var isAnimation = false
    private var xIndex = 0

    @ColorInt
    private var textColor: Int = DEFAULT_TEXT_COLOR

    @ColorInt
    private var gridColor: Int = DEFAULT_GRID_COLOR

    @ColorInt
    private var axisColor: Int = DEFAULT_AXIS_COLOR
    private var graphWidth: Float = DEFAULT_GRAPH_WIDTH
    private var axisWidth: Float = DEFAULT_AXIS_WIDTH
    private var textSize: Float = DEFAULT_TEXT_SIZE

    private var colorGraphList =
        listOf(Color.BLUE, Color.CYAN, Color.MAGENTA, Color.YELLOW, Color.RED)

    private var graphs = mutableListOf<Graph>()

    init {
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.GraphView)

            scale = ta.getFloat(R.styleable.GraphView_startScale, DEFAULT_SCALE)

            textSize = ta.getFloat(R.styleable.GraphView_textSize, DEFAULT_TEXT_SIZE)
            textColor = ta.getColor(R.styleable.GraphView_textColor, DEFAULT_TEXT_COLOR)

            gridColor = ta.getColor(R.styleable.GraphView_gridColor, DEFAULT_GRID_COLOR)

            axisColor = ta.getColor(R.styleable.GraphView_axisColor, DEFAULT_AXIS_COLOR)
            axisWidth = ta.getFloat(R.styleable.GraphView_axisWidth, DEFAULT_AXIS_WIDTH)

            graphWidth = ta.getFloat(R.styleable.GraphView_graphWidth, DEFAULT_GRAPH_WIDTH)
            ta.recycle()
        }
        graphAnimator = ValueAnimator().apply {
            duration = DURATION
            addUpdateListener {
                val value = it.animatedValue as Int
                xIndex = value
                invalidate()
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?, isReverse: Boolean) {
                    isAnimation = true
                    super.onAnimationStart(animation, isReverse)
                }

                override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
                    isAnimation = false
                    super.onAnimationEnd(animation, isReverse)
                }
            })
        }
    }

    private val gridPaint = Paint().apply {
        color = gridColor
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private val axisPaint = Paint().apply {
        color = axisColor
        style = Paint.Style.STROKE
        strokeWidth = axisWidth
        isAntiAlias = true
    }

    private val graphPaint = Paint().apply {
        color = DEFAULT_GRAPH_COLOR
        style = Paint.Style.STROKE
        strokeWidth = graphWidth
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = textColor
        style = Paint.Style.FILL_AND_STROKE
        textSize = textSize
        isAntiAlias = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measureWidth = MeasureSpec.getSize(widthMeasureSpec)
        val measureHeight = MeasureSpec.getSize(heightMeasureSpec)

        setMeasuredDimension(measureWidth, measureHeight)
    }

    override fun onDraw(canvas: Canvas) {
        drawGrid(canvas)
        drawAxis(canvas)
        drawText(canvas)
        if (isAnimation) {
            drawGraphAnimated(canvas)
        } else {
            drawGraph(canvas)
        }
    }

    /** Подписать шкалы x, y */
    private fun drawText(canvas: Canvas) {
        textPaint.textSize = textSize * scale
        textPaint.getTextBounds("0", 0, 1, textRect)
        canvas.drawText(
            "0", 0f.toX() + DEFAULT_AXIS_WIDTH,
            0f.toY() - textRect.toRectF().top + DEFAULT_AXIS_WIDTH, textPaint
        )

        val maxSide = max(measuredWidth, measuredHeight)
        val gridCount = (maxSide / gridStep) * 10
        for (x in -gridCount..gridCount) {
            if (x == 0) continue
            val text = x.toString()
            textPaint.getTextBounds(text, 0, text.length, textRect)
            val step = gridStep * x.toFloat()
            canvas.drawText(
                text, step.toX() + DEFAULT_AXIS_WIDTH,
                0f.toY() - textRect.toRectF().top + DEFAULT_AXIS_WIDTH, textPaint
            )
        }
        for (y in -gridCount..gridCount) {
            if (y == 0) continue
            val text = y.toString()
            textPaint.getTextBounds(text, 0, text.length, textRect)
            val step = gridStep * y.toFloat()
            canvas.drawText(
                text, 0f.toX() + DEFAULT_AXIS_WIDTH,
                step.toY() - textRect.toRectF().top + DEFAULT_AXIS_WIDTH, textPaint
            )
        }
    }

    /** Нарисовать сетку */
    private fun drawGrid(canvas: Canvas) {
        val maxSide = max(measuredWidth, measuredHeight)
        gridStep = (maxSide * scale / 2f).toInt()
        val gridCount = (maxSide / gridStep) * 10

        for (x in -gridCount..gridCount) {
            val step = gridStep * x.toFloat()
            canvas.drawLine(
                step.toX(),
                measuredHeight.toFloat(),
                step.toX(),
                measuredHeight.toFloat() * (-1),
                gridPaint
            )
        }
        for (y in -gridCount..gridCount) {
            val step = gridStep * y.toFloat()
            canvas.drawLine(
                measuredWidth.toFloat() * (-1),
                step.toY(),
                measuredWidth.toFloat(),
                step.toY(),
                gridPaint
            )
        }
    }

    /** Нарисовать шкалы x y */
    private fun drawAxis(canvas: Canvas) {
        canvas.drawLine(
            measuredWidth.toFloat() * (-1),
            0f.toY(),
            measuredWidth.toFloat(),
            0f.toY(),
            axisPaint
        )
        canvas.drawLine(
            0f.toX(),
            measuredHeight.toFloat(),
            0f.toX(),
            measuredHeight.toFloat() * (-1),
            axisPaint
        )
    }

    /** Нарисовать график */
    private fun drawGraph(canvas: Canvas) {
        if (graphs.isEmpty()) return
        for (graph in graphs) {
            if (graph.points.isEmpty()) return
            val path = Path()
            setStartPoint(path, graph.points)
            for ((index, xy) in graph.points.withIndex()) {
                val i = if (index + 1 < graph.points.size) index + 1 else index
                if (xy == null && graph.points[i] != null) {
                    val x = graph.points[i]!!.x * gridStep
                    val y = graph.points[i]!!.y * gridStep
                    path.moveTo(x.toX(), y.toY())
                    continue
                } else if (xy == null && graph.points[i] == null) {
                    continue
                } else {
                    val x = xy!!.x * gridStep
                    val y = xy.y * gridStep
                    path.lineTo(x.toX(), y.toY())
                }
            }
            canvas.drawPath(path, graphPaint.apply { color = graph.color })
        }
    }

    /** Нарисовать график анимированно*/
    private fun drawGraphAnimated(canvas: Canvas) {
        if (graphs.isEmpty()) return
        val graph = graphs.last()
        if (graph.points.isEmpty()) return
        val path = Path()
        setStartPoint(path, graph.points)
        for ((index, xy) in graph.points.withIndex()) {
            if (index >= xIndex) break
            val i = if (index + 1 < graph.points.size) index + 1 else index
            if (xy == null && graph.points[i] != null) {
                val x = graph.points[i]!!.x * gridStep
                val y = graph.points[i]!!.y * gridStep
                path.moveTo(x.toX(), y.toY())
                continue
            } else if (xy == null && graph.points[i] == null) {
                continue
            } else {
                val x = xy!!.x * gridStep
                val y = xy.y * gridStep
                path.lineTo(x.toX(), y.toY())
            }
        }
        canvas.drawPath(path, graphPaint.apply { color = graph.color })
    }

    private fun setStartPoint(path: Path, points: List<PointF?>) {
        for (point in points) {
            if (point != null) {
                path.moveTo(
                    (point.x * gridStep).toX(),
                    (point.y * gridStep).toY()
                )
                return
            }
        }
    }

    /** Получить случайный цвет из перечня цветов */
    private fun getRandomGraphColor(): Int = colorGraphList.random()

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                dX = event.x
                dY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                deltaX = event.x - dX
                deltaY = event.y - dY
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
            }
            else -> return false
        }
        return true
    }

    /**
     * Установить масштаб графика в диапазоне от 0 до 1, где 1 = 100%, 0,5 = 50% и т.д.
     * Изменение масштаба вызывает перерисовку.
     * @param scale масштаб.
     */
    fun setScale(scale: Float) {
        this.scale = when {
            scale > 1f -> 1f
            scale < 0.1f -> 0.1f
            else -> scale
        }
        invalidate()
    }

    /**
     * Установить цвет сетки.
     * Вызывает перерисовку.
     * @param color цвет.
     */
    fun setGridColor(@ColorInt color: Int) {
        gridPaint.color = color
        invalidate()
    }

    /**
     * Установить цвет осей.
     * Вызывает перерисовку.
     * @param color цвет.
     */
    fun setAxisColor(@ColorInt color: Int) {
        axisPaint.color = color
        invalidate()
    }

    /**
     * Установить толщину осей.
     * Вызывает перерисовку.
     * @param width толщину.
     */
    fun setAxisWidth(width: Float) {
        axisPaint.strokeWidth = width
        invalidate()
    }

    /**
     * Установить цвет текста подписи осей.
     * Вызывает перерисовку.
     * @param color цвет.
     */
    fun setTextColor(@ColorInt color: Int) {
        textPaint.color = color
        invalidate()
    }

    /**
     * Установить размер текста подписи осей.
     * Вызывает перерисовку.
     * @param size размер текста.
     */
    fun setTextSize(size: Float) {
        textSize = size
        invalidate()
    }

    /**
     * Добавить графики.
     * Вызывает перерисовку.
     * @param list - список графиков
     * @param toClear - очистить перед добавлением
     */
    fun addGraphList(list: List<Graph>, toClear: Boolean = false) {
        if (toClear) {
            graphs.clear()
        }
        graphs.addAll(list)
        invalidate()
    }

    /**
     * Добавить график.
     * Вызывает перерисовку.
     * @param graph - график
     */
    fun addGraph(graph: Graph) {
        graphs.add(graph)
        graphAnimator.setIntValues(graph.points.size - 1)
        graphAnimator.start()
    }

    /**
     * Очистить все графики.
     * Вызывает перерисовку.
     */
    fun clearGraph() {
        graphs.clear()
        invalidate()
    }

    private fun Float.toY(): Float = (this * -1f) + measuredHeight / 2f + deltaY
    private fun Float.toX(): Float = this + measuredWidth / 2f + deltaX
}