package ru.mertsalovda.core_api.dto

/**
 * Модель описывающая страну.
 *
 * @property name - назвник страны
 * @property flag - url на флаг страны типа [String]
 * @property currencies - список ходовой валюты [Currency]
 */
data class Country(
    val name: String,
    val flag: String,
    val currencies: List<Currency>,
)