package ru.mertsalovda.basic.di

import dagger.Component
import ru.mertsalovda.basic.BasicCalculatorFragment
import ru.mertsalovda.core_api.providers.ProvidersFacade

@Component(
    dependencies = [ProvidersFacade::class]
)
interface BasicCalculatorComponent {

    companion object {

        @JvmStatic
        fun create(providersFacade: ProvidersFacade): BasicCalculatorComponent {
            return DaggerBasicCalculatorComponent.builder().providersFacade(providersFacade).build()
        }
    }

    fun inject(basicCalculatorFragment: BasicCalculatorFragment)
}