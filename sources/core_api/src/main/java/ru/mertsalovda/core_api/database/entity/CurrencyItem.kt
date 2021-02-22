package ru.mertsalovda.core_api.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * Модель для отображения списка валюты
 *
 * @property countryName    назавание страны
 * @property currencyCode   символ валюты
 * @property flagUrl        флаг страны
 */
@Entity(tableName = "currency_item", primaryKeys = ["country_name", "currency_code"])
data class CurrencyItem(
    @ColumnInfo(name = "country_name")
    val countryName: String,

    @ColumnInfo(name = "currency_code")
    val currencyCode: String,

    @ColumnInfo(name = "flag_url")
    val flagUrl: String? = "",
)
