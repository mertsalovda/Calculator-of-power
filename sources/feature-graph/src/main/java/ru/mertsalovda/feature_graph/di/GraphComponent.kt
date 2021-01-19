package ru.mertsalovda.feature_graph.di

import dagger.Component
import ru.mertsalovda.feature_graph.view.GraphViewModel

@Component(
    modules = [GraphModule::class]
)
interface GraphComponent {

    companion object {

        fun create(): GraphComponent {
            return DaggerGraphComponent.builder().build()
        }
    }

    fun inject(graphViewModel: GraphViewModel)
}