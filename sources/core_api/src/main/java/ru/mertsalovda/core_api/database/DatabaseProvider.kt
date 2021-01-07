package ru.mertsalovda.core_api.database

interface DatabaseProvider {

    fun provideDatabase(): CalculatorDatabaseContract

    fun calculatorDao(): CalculatorDao
}