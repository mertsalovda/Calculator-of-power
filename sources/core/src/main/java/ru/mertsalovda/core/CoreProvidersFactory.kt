package ru.mertsalovda.core

import ru.mertsalovda.core_api.database.DatabaseProvider
import ru.mertsalovda.core_api.mediator.AppProvider
import ru.mertsalovda.core_impl.DaggerDatabaseComponent

object CoreProvidersFactory {

    fun createDatabaseBuilder(appProvider: AppProvider): DatabaseProvider {
        return DaggerDatabaseComponent.builder().appProvider(appProvider).build()
    }
}