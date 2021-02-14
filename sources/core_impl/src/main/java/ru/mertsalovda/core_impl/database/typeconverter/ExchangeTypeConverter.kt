package ru.mertsalovda.core_impl.database.typeconverter

import androidx.room.TypeConverter

/**
 * Преобразует HashMap<String, Double> в String и обратно
 * для сохранения [ExchangeRate] в БД
 */
class ExchangeTypeConverter {

    @TypeConverter
    fun fromRates(rates: HashMap<String, Double>): String {
        val result = StringBuffer()
        rates.map { "${it.key} ${it.value}" }.joinTo(result, "; ")
        return result.toString()
    }

    @TypeConverter
    fun toRates(string: String): HashMap<String, Double> {
        val result = hashMapOf<String, Double>()
        for (rate in string.split("; ")) {
            val keyValue = rate.split(" ")
            result[keyValue[0]] = keyValue[1].toDouble()
        }
        return result
    }
}