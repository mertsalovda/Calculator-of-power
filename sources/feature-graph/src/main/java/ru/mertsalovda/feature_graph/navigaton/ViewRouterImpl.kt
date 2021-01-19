package ru.mertsalovda.feature_graph.navigaton

import androidx.fragment.app.FragmentManager
import ru.mertsalovda.feature_graph.view.GraphItem
import ru.mertsalovda.feature_graph.view.dialog.NewGraphFragment
import ru.mertsalovda.feature_graph.view.graph.Graph
import javax.inject.Inject

class ViewRouterImpl @Inject constructor() : ViewRouter {

    override fun showAddNewGraphDialog(
        fragmentManager: FragmentManager,
        callback: (GraphItem) -> Unit
    ) {
        NewGraphFragment.newInstance(callback).show(fragmentManager, NewGraphFragment::class.simpleName)
    }

}