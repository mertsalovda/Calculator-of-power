package ru.mertsalovda.feature_graph.ui

import android.graphics.Color
import kotlin.random.Random

class GraphItem(
    var markerColor: Int = Color.YELLOW,
    var expression: String = "expression",
    var isVisible: Boolean = false,
) : ListItem() {
    val id: Int = Random.nextInt()
}