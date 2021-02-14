package ru.mertsalovda.core_api.dto.exchange

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Обмен валют
 * @property base   Валюта, относительно которой высчитывается курс
 * @property rates  Курс относительно базовой валюты
 * @property date   Дата обновления
 */
@Entity(tableName = "exchange_rate")
data class ExchangeRate (
    @PrimaryKey
    val base : String,
    val rates : HashMap<String, Double>,
    val date : String
)