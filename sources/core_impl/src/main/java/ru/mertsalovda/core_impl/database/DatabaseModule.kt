package ru.mertsalovda.core_impl.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.Reusable
import ru.mertsalovda.core_api.database.dao.CalculatorDao
import ru.mertsalovda.core_api.database.CalculatorDatabaseContract
import ru.mertsalovda.core_api.database.dao.CurrencyDao
import ru.mertsalovda.core_api.database.dao.ExchangeRateDao
import javax.inject.Singleton

private const val CALCULATOR_DATABASE_NAME = "CALCULATOR_DB"

@Module
class DatabaseModule {

    @Provides
    @Reusable
    fun provideCalculatorDao(calculatorDatabaseContract: CalculatorDatabaseContract): CalculatorDao {
        return calculatorDatabaseContract.calculatorDao()
    }

    @Provides
    @Reusable
    fun provideCurrencyDao(calculatorDatabaseContract: CalculatorDatabaseContract): CurrencyDao {
        return calculatorDatabaseContract.currencyDao()
    }

    @Provides
    @Reusable
    fun provideExchangeRateDao(calculatorDatabaseContract: CalculatorDatabaseContract): ExchangeRateDao {
        return calculatorDatabaseContract.exchangeRateDao()
    }

    @Provides
    @Singleton
    fun provideCalculatorDatabase(context: Context): CalculatorDatabaseContract {
        return Room.databaseBuilder(
            context,
            CalculatorDatabase::class.java, CALCULATOR_DATABASE_NAME
        ).build()
    }
}