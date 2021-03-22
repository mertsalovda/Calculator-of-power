package ru.mertsalovda.core_api.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.mertsalovda.core_api.dto.ConverterResultDto

/** API для конвертирования физических величин */
interface ConverterApi {

    /**
     * Конвертировать физическую величину
     * @param fromValue конвертируемое значение
     * @param fromUnit  единица измерения конвертируемого значения
     * @param toUnit    единица измерения результата конвертирования
     * @return          результат конвертации обёрнутый в [ConverterResultDto]
     */
    @GET("unit-conversion/")
    suspend fun convert(
        @Query("from_value") fromValue: Float,
        @Query("from_unit") fromUnit: String,
        @Query("to_unit") toUnit: String,
    ): Response<ConverterResultDto>
}