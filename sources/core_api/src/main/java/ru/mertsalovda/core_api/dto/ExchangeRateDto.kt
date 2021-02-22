package ru.mertsalovda.core_api.dto

/**
 * Обмен валют
 * @property base   Валюта, относительно которой высчитывается курс
 * @property rates  Курс относительно базовой валюты
 * @property date   Дата обновления
 */
data class ExchangeRateDto (
    val base : String,
    val rates : HashMap<String, Double>,
    val date : String
)