package ru.mertsalovda.main.di

import dagger.Component
import ru.mertsalovda.core_api.providers.ProvidersFacade
import ru.mertsalovda.main.ui.PageFragment

@Component(
    dependencies = [ProvidersFacade::class]
)
interface MainComponent {

    companion object {
        fun create(providersFacade: ProvidersFacade): MainComponent {
            return DaggerMainComponent.builder().providersFacade(providersFacade).build()
        }
    }

    fun inject(pageFragment: PageFragment)
}