package ru.mertsalovda.calculatorofpower

import dagger.Binds
import dagger.Module
import dagger.Reusable
import ru.mertsalovda.basic.navigation.BasicMediatorImpl
import ru.mertsalovda.core_api.mediator.BasicMediator
import ru.mertsalovda.core_api.mediator.MainMediator
import ru.mertsalovda.main.navigation.MainMediatorImpl

@Module
interface MediatorsBindings {

    @Binds
    @Reusable
    fun bindsMainMediator(mainMediatorImpl: MainMediatorImpl): MainMediator

    @Binds
    @Reusable
    fun bindsBasicMediator(basicMediatorImpl: BasicMediatorImpl): BasicMediator
}