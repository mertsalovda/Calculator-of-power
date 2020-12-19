package ru.mertsalovda.calculatorofpower

import dagger.Binds
import dagger.Module
import dagger.Reusable
import ro.mertsalovda.converter.navigation.ConverterMediatorImpl
import ru.mertsalovda.basic.navigation.BasicMediatorImpl
import ru.mertsalovda.core_api.mediator.BasicMediator
import ru.mertsalovda.core_api.mediator.ConverterMediator
import ru.mertsalovda.core_api.mediator.MainMediator
import ru.mertsalovda.core_api.mediator.ScientificMediator
import ru.mertsalovda.main.navigation.MainMediatorImpl
import ru.mertsalovda.scientific.navigation.ScientificMediatorImpl

@Module
interface MediatorsBindings {

    @Binds
    @Reusable
    fun bindsMainMediator(mainMediatorImpl: MainMediatorImpl): MainMediator

    @Binds
    @Reusable
    fun bindsBasicMediator(basicMediatorImpl: BasicMediatorImpl): BasicMediator

    @Binds
    @Reusable
    fun bindsScientificMediator(scientificMediatorImpl: ScientificMediatorImpl): ScientificMediator

    @Binds
    @Reusable
    fun bindsConverterMediator(converterMediatorImpl: ConverterMediatorImpl): ConverterMediator
}