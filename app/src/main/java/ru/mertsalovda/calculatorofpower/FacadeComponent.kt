package ru.mertsalovda.calculatorofpower

import android.app.Application
import dagger.Component
import ru.mertsalovda.core.CoreProvidersFactory
import ru.mertsalovda.core_api.database.DatabaseProvider
import ru.mertsalovda.core_api.providers.AppProvider
import ru.mertsalovda.core_api.providers.ProvidersFacade

@Component(
    dependencies = [AppProvider::class, DatabaseProvider::class],
    modules = [MediatorsBindings::class]
)
interface FacadeComponent : ProvidersFacade {

    companion object {
        fun init(application: Application): FacadeComponent =
            DaggerFacadeComponent.builder()
                .appProvider(AppComponent.create(application))
                .databaseProvider(CoreProvidersFactory.createDatabaseBuilder(AppComponent.create(application)))
                .build()
    }

    fun inject(app: App)
}