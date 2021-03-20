package ru.mertsalovda.core_api.database.entity

sealed class Value(open val name: String, open val code: String, open val image: String?) {
    data class Currency(
        override val name: String,
        override val code: String,
        override val image: String? = ""
    ) : Value(name, code, image)

    data class Length(
        override val name: String,
        override val code: String,
        override val image: String?
        ) : Value(name, code, image)

    data class Weight(
        override val name: String,
        override val code: String,
        override val image: String?
    ) : Value(name, code, image)

    data class Speed(
        override val name: String,
        override val code: String,
        override val image: String?
    ) : Value(name, code, image)

    data class Area(
        override val name: String,
        override val code: String,
        override val image: String?
    ) : Value(name, code, image)
}
