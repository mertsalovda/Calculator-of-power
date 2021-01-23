package ru.mertsalovda.core_api.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.mertsalovda.core_api.dto.exchange.ExchangeRate

interface ExchangeRatesApi {

    /**
     * Получить курс валют относительно базовой
     * @param base код базовой валюта (пример RUB, USD и т.д.)
     */
    @GET("latest")
    suspend fun getLatestByBaseCurrency(
        @Query("base") base: String
    ): Response<ExchangeRate>
}