package ru.mertsalovda.core_api.mediator

import ru.mertsalovda.core_api.providers.ProvidersFacade

interface AppWithFacade {

    fun getFacade(): ProvidersFacade
}