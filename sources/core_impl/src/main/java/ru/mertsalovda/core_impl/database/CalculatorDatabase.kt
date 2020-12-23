package ru.mertsalovda.core_impl.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.mertsalovda.core_api.database.CalculatorDatabaseContract
import ru.mertsalovda.core_api.dto.Expression

@Database(entities = [Expression::class], version = 1)
abstract class CalculatorDatabase : RoomDatabase(), CalculatorDatabaseContract