package ru.mertsalovda.main.ui

/**
 * Enum с перченем страниц.
 * Функция title возвращает заголовок страницы.
 */
enum class Page {
    CONVERTER {
        override fun title(): String = "Converter"
    },
    GRAPH {
        override fun title(): String = "Graph"
    };

    abstract fun title(): String
}