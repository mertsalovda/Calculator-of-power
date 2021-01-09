package ru.mertsalovda.feature_graph.view.graph

import android.graphics.PointF
import android.util.Log
import ru.mertsalovda.core_api.interfaces.Calculator
import kotlin.math.round

object GraphUtil {

    /**
     * Получить список точек для отрисовки графика
     *
     * @param startX - начальное значение X,
     * @param endX - конечное значение X,
     * @param step - шаг изменения X,
     * @param expression - функция
     * @return коллекцию точек [PointF]
     */
    fun getPoints(
        startX: Float,
        endX: Float,
        step: Float,
        expression: String,
        calculator: Calculator
    ): List<PointF?> {
        val points = mutableListOf<PointF?>()
        var x = startX
        while (x <= endX) {
            val text = replaceX(expression, round(x * 100) / 100)
            calculator.calculate(text)
            val point: PointF? = if (calculator.getResult().isInfinityOrNaN()) {
                null
            } else {
                val roundX = round(x * 100) / 100
                val roundY = round(calculator.getResult().toFloat() * 100) / 100
                PointF(roundX, roundY)
            }
            Log.d("Point", "Point $point")
            points.add(point)
            x += step
        }
        return points
    }

    /**
     * Подставляет в выражение вместо X переданое значение.
     * @param expression - выражение с переменной X.
     * @param x - значение, которое будет подставлено вместо X.
     * @return - строка с подставленным значением вместо X.
     */
    private fun replaceX(expression: String, x: Float): String {
        return if (x < 0) {
            expression.toLowerCase()
                .replace("+x", x.toString())
                .replace("*x", "*(0$x)")
                .replace("/x", "/(0$x)")
                .replace("x", "(0$x)")
        } else {
            expression.replace("x", x.toString())
        }
    }

    private fun Double.isInfinityOrNaN() =
        this == Double.POSITIVE_INFINITY || this == Double.NEGATIVE_INFINITY || this.isNaN()
}