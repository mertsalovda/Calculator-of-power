package ru.mertsalovda.feature_graph.navigaton

import androidx.fragment.app.FragmentManager
import ru.mertsalovda.feature_graph.view.GraphItem
import ru.mertsalovda.feature_graph.view.graph.Graph

interface ViewRouter {

    fun showAddNewGraphDialog(fragmentManager: FragmentManager, callback: (GraphItem) -> Unit)
}