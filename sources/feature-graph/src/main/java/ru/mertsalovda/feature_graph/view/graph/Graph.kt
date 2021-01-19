package ru.mertsalovda.feature_graph.view.graph

import android.graphics.PointF
import androidx.annotation.ColorInt

/**
 * Модель описывающая график функции
 *
 * @property expression - функция f(x)
 * @property points - x, y
 * @property color - цвет графика
 */
data class Graph(
    val expression: String,
    val points: List<PointF?>,
    @ColorInt
    val color: Int,
)
