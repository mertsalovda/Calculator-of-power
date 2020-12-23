package ru.mertsalovda.core_impl.network

import dagger.Component
import ru.mertsalovda.core_api.providers.NetworkProvider
import javax.inject.Singleton

@Singleton
@Component(
    modules = [NetworkModule::class]
)
interface NetworkComponent : NetworkProvider