package ru.mertsalovda.feature_graph.view

import android.graphics.Color
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mertsalovda.feature_graph.di.GraphComponent
import ru.mertsalovda.feature_graph.navigaton.ViewRouter
import ru.mertsalovda.feature_graph.view.graph.Graph
import javax.inject.Inject

class GraphViewModel : ViewModel() {

    @Inject
    lateinit var viewRouter: ViewRouter

    private val _graphs = MutableLiveData<MutableList<ListItem>>(mutableListOf(
        // TODO delete test data
        GraphItem(Color.YELLOW, "f(x)=x^2"),
        GraphItem(Color.BLUE, "f(x)=x+2"),
        GraphItem(Color.GREEN, "f(x)=x-2"),
        GraphItem(Color.RED, "f(x)=x-2"),)
    )
    val graph: LiveData<MutableList<ListItem>> = _graphs

    init {
        GraphComponent.create().inject(this)
    }

    fun createNewGraph(fragmentManager: FragmentManager) {
        viewRouter.showAddNewGraphDialog(fragmentManager) {
            addNewGraph(it)
        }
    }

    private fun addNewGraph(graph: Graph) {
        val graphItem = GraphItem(expression = graph.expression, markerColor = graph.color)
        _graphs.value?.let{ it.add(0, graphItem) }
        _graphs.postValue(_graphs.value)
    }

    fun deleteGraph(graphItem: GraphItem) {
        _graphs.value?.remove(graphItem)
        _graphs.postValue(_graphs.value)
    }
}