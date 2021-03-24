package ru.mertsalovda.calculatorofpower.di

import android.app.Application
import dagger.Component
import ru.mertsalovda.calculatorofpower.App
import ru.mertsalovda.core.CoreProvidersFactory
import ru.mertsalovda.core_api.database.DatabaseProvider
import ru.mertsalovda.core_api.providers.AppProvider
import ru.mertsalovda.core_api.providers.CalculatorProvider
import ru.mertsalovda.core_api.providers.NetworkProvider
import ru.mertsalovda.core_api.providers.ProvidersFacade

@Component(
    dependencies = [AppProvider::class, DatabaseProvider::class, NetworkProvider::class, CalculatorProvider::class],
    modules = [MediatorsBindings::class]
)
interface FacadeComponent : ProvidersFacade {

    companion object {
        fun init(application: Application): FacadeComponent =
            DaggerFacadeComponent.builder()
                .appProvider(AppComponent.create(application))
                .databaseProvider(CoreProvidersFactory.createDatabaseBuilder(AppComponent.create(application)))
                .networkProvider(CoreProvidersFactory.createNetworkBuilder())
                .calculatorProvider(CoreProvidersFactory.createCalculator())
                .build()
    }

    fun inject(app: App)
}