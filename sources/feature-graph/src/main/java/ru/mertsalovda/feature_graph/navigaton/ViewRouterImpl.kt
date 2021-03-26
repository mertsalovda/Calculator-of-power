package ru.mertsalovda.feature_graph.navigaton

import androidx.fragment.app.FragmentManager
import ru.mertsalovda.feature_graph.ui.GraphItem
import ru.mertsalovda.feature_graph.ui.dialog.NewGraphFragment
import javax.inject.Inject

class ViewRouterImpl @Inject constructor() : ViewRouter {

    override fun showAddNewGraphDialog(
        fragmentManager: FragmentManager,
        callback: (GraphItem) -> Unit
    ) {
        NewGraphFragment.newInstance(callback).show(fragmentManager, NewGraphFragment::class.simpleName)
    }

}