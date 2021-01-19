package ru.mertsalovda.core_api.providers

import ru.mertsalovda.core_api.network.CountriesApi
import ru.mertsalovda.core_api.network.ExchangeRatesApi
import ru.mertsalovda.core_api.network.NewtonApi

interface NetworkProvider {

    fun provideCountriesApi(): CountriesApi

    fun provideNewtonApi(): NewtonApi

    fun provideExchangeRatesApi(): ExchangeRatesApi
}