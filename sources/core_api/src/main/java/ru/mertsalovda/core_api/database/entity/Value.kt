package ru.mertsalovda.core_api.database.entity

/**
 * Физическая величина
 *
 * @property name   название
 * @property code   сокращённое название
 * @property image  изображение
 * @property unit   название на английском
 */
sealed class Value(
    open val name: String,
    open val code: String,
    open val image: String?,
    open val unit: String
) {
    data class Currency(
        override val name: String,
        override val code: String,
        override val image: String? = "",
        override val unit: String
    ) : Value(name, code, image, unit)

    data class Length(
        override val name: String,
        override val code: String,
        override val image: String?,
        override val unit: String
    ) : Value(name, code, image, unit)

    data class Weight(
        override val name: String,
        override val code: String,
        override val image: String?,
        override val unit: String
    ) : Value(name, code, image, unit)

    data class Speed(
        override val name: String,
        override val code: String,
        override val image: String?,
        override val unit: String
    ) : Value(name, code, image, unit)

    data class Area(
        override val name: String,
        override val code: String,
        override val image: String?,
        override val unit: String
    ) : Value(name, code, image, unit)
}
