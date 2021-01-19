package ru.mertsalovda.core_api.interfaces

interface Calculator {

    fun calculate(expression: String)
    fun getResult(): Double
}