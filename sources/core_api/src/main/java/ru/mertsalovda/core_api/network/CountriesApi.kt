package ru.mertsalovda.core_api.network

import retrofit2.Response
import retrofit2.http.GET
import ru.mertsalovda.core_api.dto.Country

interface CountriesApi {

    /**
     * Запрос на получение списка всех стран.
     * Фильтр NAME, FLAG, CURRENCIES.
     *
     * @return списк всех стран.
     */
    @GET("all?fields=name;flag;currencies")
    suspend fun getAllCountries() : Response<List<Country>>
}