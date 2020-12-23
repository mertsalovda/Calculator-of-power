package ru.mertsalovda.core

import ru.mertsalovda.core_api.database.DatabaseProvider
import ru.mertsalovda.core_api.providers.AppProvider
import ru.mertsalovda.core_api.providers.NetworkProvider
import ru.mertsalovda.core_impl.database.DaggerDatabaseComponent
import ru.mertsalovda.core_impl.network.DaggerNetworkComponent

object CoreProvidersFactory {

    fun createDatabaseBuilder(appProvider: AppProvider): DatabaseProvider {
        return DaggerDatabaseComponent.builder().appProvider(appProvider).build()
    }

    fun createNetworkBuilder(): NetworkProvider {
        return DaggerNetworkComponent.builder().build()
    }
}