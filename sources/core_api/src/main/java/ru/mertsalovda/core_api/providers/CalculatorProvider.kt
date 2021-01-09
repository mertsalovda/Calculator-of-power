package ru.mertsalovda.core_api.providers

import ru.mertsalovda.core_api.interfaces.Calculator

interface CalculatorProvider {

    fun provideCalculator(): Calculator
}