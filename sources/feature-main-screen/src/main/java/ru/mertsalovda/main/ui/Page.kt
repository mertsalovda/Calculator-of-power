package ru.mertsalovda.main.ui

/**
 * Enum с перченем страниц.
 * Функция title возвращает заголовок страницы.
 */
enum class Page {
    CONVERTER {
        override fun title(): String = "Converter"
    },
    BASIC_CALCULATOR {
        override fun title(): String = "Basic"
    },
    SCIENTIFIC_CALCULATOR {
        override fun title(): String = "Scientific"
    };

    abstract fun title(): String
}