package ru.mertsalovda.core_impl.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.Reusable
import ru.mertsalovda.core_api.database.CalculatorDao
import ru.mertsalovda.core_api.database.CalculatorDatabaseContract
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
    @Singleton
    fun provideCalculatorDatabase(context: Context): CalculatorDatabaseContract {
        return Room.databaseBuilder(
            context,
            CalculatorDatabase::class.java, CALCULATOR_DATABASE_NAME
        ).build()
    }
}