package ru.mertsalovda.feature_graph.navigaton

import androidx.fragment.app.FragmentManager
import ru.mertsalovda.core_api.mediator.GraphMediator
import ru.mertsalovda.feature_graph.GraphFragment
import javax.inject.Inject

class GraphMediatorImpl @Inject constructor() : GraphMediator {
    override fun openGraphScreen(containerId: Int, fragmentManager: FragmentManager) {
        fragmentManager.beginTransaction()
            .add(containerId, GraphFragment.newInstance())
            .commit()
    }
}