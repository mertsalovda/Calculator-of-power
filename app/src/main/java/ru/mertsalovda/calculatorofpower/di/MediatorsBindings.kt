package ru.mertsalovda.calculatorofpower.di

import dagger.Binds
import dagger.Module
import dagger.Reusable
import ro.mertsalovda.converter.navigation.ConverterMediatorImpl
import ru.mertsalovda.basic.navigation.BasicCalculatorMediatorImpl
import ru.mertsalovda.core_api.mediator.*
import ru.mertsalovda.feature_graph.navigaton.GraphMediatorImpl
import ru.mertsalovda.main.navigation.MainMediatorImpl
import ru.mertsalovda.scientific.navigation.ScientificCalculatorMediatorImpl

@Module
interface MediatorsBindings {

    @Binds
    @Reusable
    fun bindsMainMediator(mainMediatorImpl: MainMediatorImpl): MainMediator

    @Binds
    @Reusable
    fun bindsBasicMediator(basicMediatorImpl: BasicCalculatorMediatorImpl): BasicCalculatorMediator

    @Binds
    @Reusable
    fun bindsScientificMediator(scientificMediatorImpl: ScientificCalculatorMediatorImpl): ScientificCalculatorMediator

    @Binds
    @Reusable
    fun bindsConverterMediator(converterMediatorImpl: ConverterMediatorImpl): ConverterMediator

    @Binds
    @Reusable
    fun bindsGraphMediator(graphMediatorImpl: GraphMediatorImpl): GraphMediator
}