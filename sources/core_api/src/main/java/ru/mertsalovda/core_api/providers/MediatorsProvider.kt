package ru.mertsalovda.core_api.providers

import ru.mertsalovda.core_api.mediator.BasicCalculatorMediator
import ru.mertsalovda.core_api.mediator.ConverterMediator
import ru.mertsalovda.core_api.mediator.MainMediator
import ru.mertsalovda.core_api.mediator.ScientificCalculatorMediator

interface MediatorsProvider {

    fun provideMainMediator(): MainMediator

    fun provideBasicMediator(): BasicCalculatorMediator

    fun provideScientificMediator(): ScientificCalculatorMediator

    fun provideConverterMediator(): ConverterMediator
}