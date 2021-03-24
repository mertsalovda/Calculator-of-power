package ru.mertsalovda.core_impl.calculator

import dagger.Binds
import dagger.Module
import dagger.Reusable
import ru.mertsalovda.core_api.interfaces.Calculator

@Module
interface CalculatorModule {

    @Binds
    @Reusable
    fun bindsCalculator(calculatorImpl: CalculatorImpl): Calculator
}