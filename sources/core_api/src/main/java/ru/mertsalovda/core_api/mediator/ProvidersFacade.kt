package ru.mertsalovda.core_api.mediator

import ru.mertsalovda.core_api.database.DatabaseProvider

/**
 * ProvidersFacade приводит провайдеры к общему интерфейсу
 */
interface ProvidersFacade : MediatorsProvider, AppProvider, DatabaseProvider