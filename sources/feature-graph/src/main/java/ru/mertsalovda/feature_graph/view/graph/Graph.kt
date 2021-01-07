package ru.mertsalovda.feature_graph.view.graph

import android.graphics.PointF
import androidx.annotation.ColorInt

/**
 * Модель описывающая график функции
 *
 * @property expression - функция f(x)
 * @property points - x, y
 * @property color - цвет графика
 * @property isSelected - указывает на то, что график выделен будет нарисован поверх других графиков
 */
data class Graph(
    val expression: String,
    val points: List<PointF>,
    @ColorInt
    var color: Int,
    var isSelected: Boolean = false
)
