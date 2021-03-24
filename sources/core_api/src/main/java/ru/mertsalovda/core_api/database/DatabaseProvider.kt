package ru.mertsalovda.core_api.database

import ru.mertsalovda.core_api.database.dao.CalculatorDao
import ru.mertsalovda.core_api.database.dao.CurrencyDao
import ru.mertsalovda.core_api.database.dao.ExchangeRateDao

interface DatabaseProvider {

    fun provideDatabase(): CalculatorDatabaseContract

    fun calculatorDao(): CalculatorDao

    fun exchangeRateDao(): ExchangeRateDao

    fun currencyDao(): CurrencyDao
}