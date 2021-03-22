package ru.mertsalovda.core_api.dto

import com.google.gson.annotations.SerializedName

/**
 * Результат конвертирования величин
 * @property result    результат
 */
data class ConverterResultDto(

    @SerializedName("results")
    val result : Result
)

/**
 * Результат конвертирования величин
 */
data class Result (

    /** Представление конвертируемых величин в виде выражения */
    @SerializedName("display_value")
    val displayValue : String,

    /** Чилсловое значение результата конвертирования */
    @SerializedName("float_value")
    val floatValue : Float
)