package ru.mertsalovda.basic.di

import dagger.Component
import ru.mertsalovda.basic.BasicFragment
import ru.mertsalovda.core_api.mediator.ProvidersFacade

@Component(
    dependencies = [ProvidersFacade::class]
)
interface BasicComponent {

    companion object {

        @JvmStatic
        fun create(providersFacade: ProvidersFacade): BasicComponent {
            return DaggerBasicComponent.builder().providersFacade(providersFacade).build()
        }
    }

    fun inject(basicFragment: BasicFragment)
}