package ru.mertsalovda.feature_graph.ui

import android.graphics.Color
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mertsalovda.core.CoreProvidersFactory
import ru.mertsalovda.core_api.interfaces.Calculator
import ru.mertsalovda.feature_graph.di.GraphComponent
import ru.mertsalovda.feature_graph.navigaton.ViewRouter
import ru.mertsalovda.feature_graph.view.graph.Graph
import ru.mertsalovda.feature_graph.view.graph.GraphUtil
import javax.inject.Inject

class GraphViewModel : ViewModel() {

    @Inject
    lateinit var viewRouter: ViewRouter

    private var calculator: Calculator

    private val _graphItems = MutableLiveData<MutableList<ListItem>>(
        mutableListOf(
            // TODO delete test data
            GraphItem(Color.CYAN, "x^2"),
            GraphItem(Color.YELLOW, "sin(x)"),
            GraphItem(Color.GREEN, "2/x"),
            GraphItem(Color.RED, "sqrt((16-x^2))"),
            GraphItem(Color.RED, "-sqrt((16-x^2))"),
            GraphItem(Color.RED, "4-2*x"),
            GraphItem(Color.RED, "2*x+4"),
            GraphItem(Color.RED, "0*x+2"),
            GraphItem(Color.RED, "(2/3)*(x-5)+3"),
            GraphItem(Color.RED, "((-7)/10)*(x+5)+3"),
        )
    )
    val graphItems: LiveData<MutableList<ListItem>> = _graphItems

    private val _graph = MutableLiveData<Graph?>()
    val graph: LiveData<Graph?> = _graph

    private val _graphs = MutableLiveData<List<Graph>>()
    val graphs: LiveData<List<Graph>> = _graphs

    init {
        GraphComponent.create().inject(this)
        calculator = CoreProvidersFactory.createCalculator().provideCalculator()
    }

    /** Вызвать редактор функции и добавить новую функцию в список */
    fun createNewGraph(fragmentManager: FragmentManager) {
        viewRouter.showAddNewGraphDialog(fragmentManager) {
            addNewGraph(it)
        }
    }

    /** Добавить новую фукцию в список */
    private fun addNewGraph(graphItem: GraphItem) {
        _graphItems.value?.let { it.add(0, graphItem) }
        _graphItems.postValue(_graphItems.value)
    }

    /** Удалить график из списка */
    fun deleteGraph(graphItem: GraphItem) {
        _graphItems.value?.remove(graphItem)
        _graphItems.postValue(_graphItems.value)
        drawAllGraph()
    }

    /** Нарисовать график */
    fun drawGraph(graphItem: GraphItem) {
        if (graphItem.isVisible) {
            val points =
                GraphUtil.getPoints(-10.00f, 10.00f, 0.10f, graphItem.expression, calculator)
            _graph.postValue(Graph(graphItem.expression, points, graphItem.markerColor))
        }
    }

    /** Нарисовать все графики у которых isVisible = true*/
    fun drawAllGraph() {
        val graphItems = graphItems.value as List<GraphItem>
        val graphList = mutableListOf<Graph>()
        for (graphItem in graphItems.filter { it.isVisible }) {
            val points =
                GraphUtil.getPoints(-10.00f, 10.00f, 0.10f, graphItem.expression, calculator)
            graphList.add(Graph(graphItem.expression, points, graphItem.markerColor))
        }
        _graphs.postValue(graphList)
    }
}