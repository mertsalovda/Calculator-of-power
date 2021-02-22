package ru.mertsalovda.core_api.database

import ru.mertsalovda.core_api.database.dao.CalculatorDao
import ru.mertsalovda.core_api.database.dao.CurrencyDao
import ru.mertsalovda.core_api.database.dao.ExchangeRateDao

interface CalculatorDatabaseContract {

    /** DAO для работы с математическими выражениями. */
    fun calculatorDao(): CalculatorDao

    /** DAO для работы с валютой. */
    fun currencyDao(): CurrencyDao

    /** DAO для работы с обменным курсом валют. */
    fun exchangeRateDao(): ExchangeRateDao
}