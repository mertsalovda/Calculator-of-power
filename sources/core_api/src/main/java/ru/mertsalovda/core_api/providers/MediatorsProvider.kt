package ru.mertsalovda.core_api.providers

import ru.mertsalovda.core_api.mediator.*

interface MediatorsProvider {

    fun provideMainMediator(): MainMediator

    fun provideBasicMediator(): BasicCalculatorMediator

    fun provideScientificMediator(): ScientificCalculatorMediator

    fun provideConverterMediator(): ConverterMediator

    fun provideGraphMediator(): GraphMediator
}