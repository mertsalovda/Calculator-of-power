package ru.mertsalovda.core_api.dto

/**
 * Модель описывает валюту
 *
 * @property code   сокращённое наименование валюты
 * @property name   название валюты
 * @property symbol символьное обозначение валюты
 */
data class CurrencyDto(
    val code: String?,
    val name: String,
    val symbol: String
)