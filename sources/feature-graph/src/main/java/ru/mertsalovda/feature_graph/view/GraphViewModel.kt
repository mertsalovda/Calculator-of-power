package ru.mertsalovda.feature_graph.view

import android.graphics.Color
import android.graphics.PointF
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mertsalovda.core.CoreProvidersFactory
import ru.mertsalovda.core_api.interfaces.Calculator
import ru.mertsalovda.feature_graph.di.GraphComponent
import ru.mertsalovda.feature_graph.navigaton.ViewRouter
import ru.mertsalovda.feature_graph.view.graph.Graph
import javax.inject.Inject
import kotlin.math.round

class GraphViewModel : ViewModel() {

    @Inject
    lateinit var viewRouter: ViewRouter

    private var calculator: Calculator

    private val _graphItems = MutableLiveData<MutableList<ListItem>>(
        mutableListOf(
            // TODO delete test data
            GraphItem(Color.CYAN, "x^2"),
            GraphItem(Color.YELLOW, "sin(x)"),
            GraphItem(Color.BLUE, "x+2"),
            GraphItem(Color.GREEN, "2*x"),
            GraphItem(Color.RED, "2/x"),
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
    private fun addNewGraph(graph: Graph) {
        val graphItem = GraphItem(expression = graph.expression, markerColor = graph.color)
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
            val points = getPoints(-10.00f, 10.00f, 0.10f, graphItem.expression)
            _graph.postValue(Graph(graphItem.expression, points, graphItem.markerColor))
        }
    }

    /** Нарисовать все графики у которых isVisible = true*/
    fun drawAllGraph() {
        val graphItems = graphItems.value as List<GraphItem>
        val graphList = mutableListOf<Graph>()
        for (graphItem in graphItems.filter { it.isVisible }) {
            val points = getPoints(-10.00f, 10.00f, 0.10f, graphItem.expression)
            graphList.add(Graph(graphItem.expression, points, graphItem.markerColor))
        }
        _graphs.postValue(graphList)
    }

    /**
     * Получить список точек для отрисовки графика
     *
     * @param startX - начальное значение X,
     * @param endX - конечное значение X,
     * @param step - шаг изменения X,
     * @param expression - функция
     * @return коллекцию точек [PointF]
     */
    private fun getPoints(
        startX: Float,
        endX: Float,
        step: Float,
        expression: String
    ): List<PointF?> {
        val points = mutableListOf<PointF?>()
        var x = startX
        while (x <= endX) {
            val text = replaceX(expression, round(x * 100) / 100)
            calculator.calculate(text)
            val point: PointF? = if (calculator.getResult().isInfinityOrNaN()) {
                null
            } else {
                val roundX = round(x * 100) / 100
                val roundY = round(calculator.getResult().toFloat() * 100) / 100
                PointF(roundX, roundY)
            }
            Log.d("Point", "Point $point")
            points.add(point)
            x += step
        }
        return points
    }

    /**
     * Подставляет в выражение вместо X переданое значение.
     * @param expression - выражение с переменной X.
     * @param x - значение, которое будет подставлено вместо X.
     * @return - строка с подставленным значением вместо X.
     */
    private fun replaceX(expression: String, x: Float): String {
        return if (x < 0) {
            expression.toLowerCase()
                .replace("+x", x.toString())
                .replace("*x", "*(0$x)")
                .replace("/x", "/(0$x)")
                .replace("x", "0$x")
        } else {
            expression.replace("x", x.toString())
        }
    }

    private fun Double.isInfinityOrNaN() =
        this == Double.POSITIVE_INFINITY || this == Double.NEGATIVE_INFINITY || this.isNaN()
}