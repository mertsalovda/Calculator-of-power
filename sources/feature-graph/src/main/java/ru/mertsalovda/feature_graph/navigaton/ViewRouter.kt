package ru.mertsalovda.feature_graph.navigaton

import androidx.fragment.app.FragmentManager
import ru.mertsalovda.feature_graph.ui.GraphItem

interface ViewRouter {

    fun showAddNewGraphDialog(fragmentManager: FragmentManager, callback: (GraphItem) -> Unit)
}