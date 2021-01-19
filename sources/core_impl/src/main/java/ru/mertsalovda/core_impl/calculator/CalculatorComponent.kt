package ru.mertsalovda.core_impl.calculator

import dagger.Component
import ru.mertsalovda.core_api.providers.CalculatorProvider
import javax.inject.Singleton

@Singleton
@Component(
    modules = [CalculatorModule::class]
)
interface CalculatorComponent : CalculatorProvider{
}