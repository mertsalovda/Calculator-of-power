package ru.mertsalovda.feature_graph.view.graph

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.graphics.toRectF
import kotlin.math.max

class GraphView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        const val DEFAULT_SCALE = 0.2f

        const val DEFAULT_GRID_COLOR = Color.GREEN

        const val DEFAULT_AXIS_WIDTH = 6f
        const val DEFAULT_AXIS_COLOR = Color.RED

        const val DEFAULT_GRAPH_WIDTH = 6f
        const val DEFAULT_GRAPH_COLOR = Color.YELLOW

        const val DEFAULT_TEXT_SIZE = 130f
        const val DEFAULT_TEXT_COLOR = Color.BLUE
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

    private var textSize: Float = DEFAULT_TEXT_SIZE

    private var xlist = listOf<Float>(-10.0f, -9.9f, -9.8f, -9.7f, -9.6f, -9.5f, -9.4f, -9.3f, -9.2f, -9.1f, -9.0f, -8.9f, -8.8f, -8.7f, -8.6f, -8.5f, -8.4f, -8.3f, -8.2f, -8.1f, -8.0f, -7.9f, -7.8f, -7.7f, -7.6f, -7.5f, -7.4f, -7.3f, -7.2f, -7.1f, -7.0f, -6.9f, -6.8f, -6.7f, -6.6f, -6.5f, -6.4f, -6.3f, -6.2f, -6.1f, -6.0f, -5.9f, -5.8f, -5.7f, -5.6f, -5.5f, -5.4f, -5.3f, -5.2f, -5.1f, -5.0f, -4.9f, -4.8f, -4.7f, -4.6f, -4.5f, -4.4f, -4.3f, -4.2f, -4.1f, -4.0f, -3.9f, -3.8f, -3.7f, -3.6f, -3.5f, -3.4f, -3.3f, -3.2f, -3.1f, -3.0f, -2.9f, -2.8f, -2.7f, -2.6f, -2.5f, -2.4f, -2.3f, -2.2f, -2.1f, -2.0f, -1.9f, -1.8f, -1.7f, -1.6f, -1.5f, -1.4f, -1.3f, -1.2f, -1.1f, -1.0f, -0.9f, -0.8f, -0.7f, -0.6f, -0.5f, -0.4f, -0.3f, -0.2f, -0.1f, 0.0f,  0.1f,  0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f, 1.1f, 1.2f, 1.3f, 1.4f, 1.5f, 1.6f, 1.7f, 1.8f, 1.9f, 2.0f, 2.1f, 2.2f, 2.3f, 2.4f, 2.5f, 2.6f, 2.7f, 2.8f, 2.9f, 3.0f, 3.1f, 3.2f, 3.3f, 3.4f, 3.5f, 3.6f, 3.7f, 3.8f, 3.9f, 4.0f, 4.1f, 4.2f, 4.3f, 4.4f, 4.5f, 4.6f, 4.7f, 4.8f, 4.9f, 5.0f, 5.1f, 5.2f, 5.3f, 5.4f, 5.5f, 5.6f, 5.7f, 5.8f, 5.9f, 6.0f, 6.1f, 6.2f, 6.3f, 6.4f, 6.5f, 6.6f, 6.7f, 6.8f, 6.9f, 7.0f, 7.1f, 7.2f, 7.3f, 7.4f, 7.5f, 7.6f, 7.7f, 7.8f, 7.9f, 8.0f, 8.1f, 8.2f, 8.3f, 8.4f, 8.5f, 8.6f, 8.7f, 8.8f, 8.9f, 9.0f, 9.1f, 9.2f, 9.3f, 9.4f, 9.5f, 9.6f, 9.7f, 9.8f, 9.9f, 10.0f)
    private var ylist = listOf<Float>(100.00f, 98.01f, 96.04f, 94.09f, 92.16f, 90.25f, 88.36f, 86.49f, 84.64f, 82.81f, 81.00f, 79.21f, 77.44f, 75.69f, 73.96f, 72.25f, 70.56f, 68.89f, 67.24f, 65.61f, 64.00f, 62.41f, 60.84f, 59.29f, 57.76f, 56.25f, 54.76f, 53.29f, 51.84f, 50.41f, 49.00f, 47.61f, 46.24f, 44.89f, 43.56f, 42.25f, 40.96f, 39.69f, 38.44f, 37.21f, 36.00f, 34.81f, 33.64f, 32.49f, 31.36f, 30.25f, 29.16f, 28.09f, 27.04f, 26.01f, 25.00f, 24.01f, 23.04f, 22.09f, 21.16f, 20.25f, 19.36f, 18.49f, 17.64f, 16.81f, 16.00f, 15.21f, 14.44f, 13.69f, 12.96f, 12.25f, 11.56f, 10.89f, 10.24f, 9.61f, 9.00f, 8.41f, 7.84f, 7.29f, 6.76f, 6.25f, 5.76f, 5.29f, 4.84f, 4.41f, 4.00f, 3.61f, 3.24f, 2.89f, 2.56f, 2.25f, 1.96f, 1.69f, 1.44f, 1.21f, 1.00f, 0.81f, 0.64f, 0.49f, 0.36f, 0.25f, 0.16f, 0.09f, 0.04f, 0.01f, 0.00f, 0.01f, 0.04f, 0.09f, 0.16f, 0.25f, 0.36f, 0.49f, 0.64f, 0.81f, 1.00f, 1.21f, 1.44f, 1.69f, 1.96f, 2.25f, 2.56f, 2.89f, 3.24f, 3.61f, 4.00f, 4.41f, 4.84f, 5.29f, 5.76f, 6.25f, 6.76f, 7.29f, 7.84f, 8.41f, 9.00f, 9.61f, 10.24f, 10.89f, 11.56f, 12.25f, 12.96f, 13.69f, 14.44f, 15.21f, 16.00f, 16.81f, 17.64f, 18.49f, 19.36f, 20.25f, 21.16f, 22.09f, 23.04f, 24.01f, 25.00f, 26.01f, 27.04f, 28.09f, 29.16f, 30.25f, 31.36f, 32.49f, 33.64f, 34.81f, 36.00f, 37.21f, 38.44f, 39.69f, 40.96f, 42.25f, 43.56f, 44.89f, 46.24f, 47.61f, 49.00f, 50.41f, 51.84f, 53.29f, 54.76f, 56.25f, 57.76f, 59.29f, 60.84f, 62.41f, 64.00f, 65.61f, 67.24f, 68.89f, 70.56f, 72.25f, 73.96f, 75.69f, 77.44f, 79.21f, 81.00f, 82.81f, 84.64f, 86.49f, 88.36f, 90.25f, 92.16f, 94.09f, 96.04f, 98.01f, 100.00f)

    private var graphPath: MutableList<PointF> = mutableListOf()

    init {
        graphPath = mutableListOf()
        for ((index, x) in xlist.withIndex()) {
            graphPath.add(PointF(x, ylist[index]))
        }
    }

    private val gridPaint = Paint().apply {
        color = DEFAULT_GRID_COLOR
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private val axisPaint = Paint().apply {
        color = DEFAULT_AXIS_COLOR
        style = Paint.Style.STROKE
        strokeWidth = DEFAULT_AXIS_WIDTH
        isAntiAlias = true
    }

    private val graphPaint = Paint().apply {
        color = DEFAULT_GRAPH_COLOR
        style = Paint.Style.STROKE
        strokeWidth = DEFAULT_GRAPH_WIDTH
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = DEFAULT_TEXT_COLOR
        style = Paint.Style.FILL_AND_STROKE
        textSize = DEFAULT_TEXT_SIZE
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
        drawGraph(canvas)
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
        gridStep = (maxSide * scale  / 2f).toInt()
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
        if (graphPath.isEmpty()) return
        val path = Path()
        path.moveTo((graphPath[0].x * gridStep).toX(), (graphPath[0].y * gridStep).toY())
        for (xy in graphPath) {
            val x = xy.x * gridStep
            val y = xy.y * gridStep
            path.lineTo(x.toX(), y.toY())
        }
        canvas.drawPath(path, graphPaint)
    }

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

    private fun Float.toY(): Float = (this * -1f) + measuredHeight / 2f + deltaY
    private fun Float.toX(): Float = this + measuredWidth / 2f + deltaX
}