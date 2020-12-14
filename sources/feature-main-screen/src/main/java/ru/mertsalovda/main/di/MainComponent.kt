package ru.mertsalovda.main.di

import dagger.Component
import ru.mertsalovda.core_api.mediator.ProvidersFacade
import ru.mertsalovda.main.MainActivity

@Component(
    dependencies = [ProvidersFacade::class]
)
interface MainComponent {

    companion object {
        fun create(providersFacade: ProvidersFacade): MainComponent {
            return DaggerMainComponent.builder().providersFacade(providersFacade).build()
        }
    }

    fun inject(mainActivity: MainActivity)
}