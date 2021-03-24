package ru.mertsalovda.feature_graph.di

import dagger.Binds
import dagger.Module
import dagger.Reusable
import ru.mertsalovda.feature_graph.navigaton.ViewRouter
import ru.mertsalovda.feature_graph.navigaton.ViewRouterImpl

@Module
interface GraphModule {

    @Binds
    @Reusable
    fun bindsGraphMediator(viewRouter: ViewRouterImpl): ViewRouter
}