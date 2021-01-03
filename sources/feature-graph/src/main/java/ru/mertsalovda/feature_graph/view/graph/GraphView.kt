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
        const val DEFAULT_SCALE = 0.1f

        const val DEFAULT_GRID_COLOR = Color.GREEN

        const val DEFAULT_AXIS_WIDTH = 6f
        const val DEFAULT_AXIS_COLOR = Color.RED

        const val DEFAULT_GRAPH_WIDTH = 6f
        const val DEFAULT_GRAPH_COLOR = Color.YELLOW

        const val DEFAULT_TEXT_SIZE = 30f
        const val DEFAULT_TEXT_COLOR = Color.BLUE
    }

    /** Масштаб графика */
    private var scale: Float = DEFAULT_SCALE

    private var gridStep: Int = 0
    private var deltaX: Float = 0f
    private var deltaY: Float = 0f
    private var dX = 0f
    private var dY = 0f

    private val textRect = Rect()

    private var graphPath: List<PointF> = listOf(
        PointF(-6f, 36f),
        PointF(-5f, 25f),
        PointF(-4f, 16f),
        PointF(-3f, 9f),
        PointF(-2f, 4f),
        PointF(-1f, 1f),
        PointF(0f, 0f),
        PointF(1f, 1f),
        PointF(2f, 4f),
        PointF(3f, 9f),
        PointF(4f, 16f),
        PointF(5f, 25f),
        PointF(6f, 36f),
    )

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
        textPaint.getTextBounds("0", 0, 1, textRect)
        canvas.drawText(
            "0", 0f.toX() + DEFAULT_AXIS_WIDTH,
            0f.toY() - textRect.toRectF().top + DEFAULT_AXIS_WIDTH, textPaint
        )

        val gridXCount = measuredWidth / gridStep
        val gridYCount = measuredHeight / gridStep
        for (x in -gridXCount..gridXCount) {
            if (x == 0) continue
            val text = x.toString()
            textPaint.getTextBounds(text, 0, text.length, textRect)
            val step = gridStep * x.toFloat()
            canvas.drawText(
                text, step.toX() + DEFAULT_AXIS_WIDTH,
                0f.toY() - textRect.toRectF().top + DEFAULT_AXIS_WIDTH, textPaint
            )
        }
        for (y in -gridYCount..gridYCount) {
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
        // Размеры сетки вычисляются по большей стороне
        val maxSide = max(measuredWidth, measuredHeight)
        gridStep = (maxSide * scale).toInt()
        val gridXCount = measuredWidth / gridStep
        val gridYCount = measuredHeight / gridStep

        for (x in -gridXCount..gridXCount) {
            val step = gridStep * x.toFloat()
            canvas.drawLine(
                step.toX(),
                measuredHeight.toFloat(),
                step.toX(),
                measuredHeight.toFloat().toY(),
                gridPaint
            )
        }
        for (y in -gridYCount..gridYCount) {
            val step = gridStep * y.toFloat()
            canvas.drawLine(
                measuredWidth.toFloat() * (-1),
                step.toY(),
                measuredWidth.toFloat().toX(),
                step.toY(),
                gridPaint
            )
        }
    }

    /** Нарисовать шкалы x y */
    private fun drawAxis(canvas: Canvas) {
        canvas.drawLine(
            measuredWidth.toFloat() * (-1), 0f.toY(),
            measuredWidth.toFloat().toX(), 0f.toY(), axisPaint
        )
        canvas.drawLine(
            0f.toX(), measuredHeight.toFloat(),
            0f.toX(), measuredHeight.toFloat().toY(), axisPaint
        )
    }

    /** Нарисовать график */
    private fun drawGraph(canvas: Canvas) {
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
        this.scale = scale
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
        textPaint.textSize = size
        invalidate()
    }

    private fun Float.toY(): Float = (this * -1f) + measuredHeight / 2f + deltaY
    private fun Float.toX(): Float = this + measuredWidth / 2f + deltaX
}