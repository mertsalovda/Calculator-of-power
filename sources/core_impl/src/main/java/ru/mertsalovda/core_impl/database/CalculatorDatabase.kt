package ru.mertsalovda.core_impl.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.mertsalovda.core_api.database.CalculatorDatabaseContract
import ru.mertsalovda.core_api.database.entity.CurrencyItem
import ru.mertsalovda.core_api.database.entity.Expression
import ru.mertsalovda.core_api.database.entity.ExchangeRate
import ru.mertsalovda.core_impl.database.typeconverter.ExchangeTypeConverter

@Database(
    entities = [Expression::class, CurrencyItem::class, ExchangeRate::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ExchangeTypeConverter::class)
abstract class CalculatorDatabase : RoomDatabase(), CalculatorDatabaseContract