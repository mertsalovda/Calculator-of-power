package ru.mertsalovda.core_api.dto.exchange

data class ExchangeRate (
    val rates : HashMap<String, Double>,
    val base : String,
    val date : String
)