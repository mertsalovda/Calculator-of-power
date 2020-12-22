package ru.mertsalovda.core_api.providers

import ru.mertsalovda.core_api.network.CountriesApi
import ru.mertsalovda.core_api.network.ForeignExchangeRatesApi
import ru.mertsalovda.core_api.network.NewtonApi

interface NetworkProvider {

    fun provideCountriesApi(): CountriesApi

    fun provideNewtonApi(): NewtonApi

    fun provideForeignExchangeRatesApi(): ForeignExchangeRatesApi
}