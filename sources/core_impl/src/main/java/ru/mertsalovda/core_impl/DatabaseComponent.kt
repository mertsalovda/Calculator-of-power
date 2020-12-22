package ru.mertsalovda.core_impl

import dagger.Component
import ru.mertsalovda.core_api.database.DatabaseProvider
import ru.mertsalovda.core_api.providers.AppProvider
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [AppProvider::class],
    modules = [DatabaseModule::class]
)
interface DatabaseComponent : DatabaseProvider