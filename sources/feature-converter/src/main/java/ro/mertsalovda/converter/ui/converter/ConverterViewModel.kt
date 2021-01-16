package ro.mertsalovda.converter.ui.converter

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ro.mertsalovda.converter.di.ConverterComponent
import ro.mertsalovda.converter.navigation.ViewRouter
import ro.mertsalovda.converter.ui.currency.CurrencyItem
import javax.inject.Inject

class ConverterViewModel : ViewModel() {

    @Inject
    lateinit var viewRouter: ViewRouter

    private val _unit1 = MutableLiveData<String>("0")
    val unit1: LiveData<String> = _unit1

    private val _unit2 = MutableLiveData<String>("0")
    val unit2: LiveData<String> = _unit2

    private var selectedValue = Value.CONVERTED_VALUE

    private val _unitPreview1 = MutableLiveData<CurrencyItem?>(null)
    val unitPreview1: LiveData<CurrencyItem?> = _unitPreview1

    private val _unitPreview2 = MutableLiveData<CurrencyItem?>(null)
    val unitPreview2: LiveData<CurrencyItem?> = _unitPreview2

    init {
        ConverterComponent.create().inject(this)
    }

    /** Указать какая величина в фокусе */
    fun setValueFocused(value: Value) {
        this.selectedValue = value
    }

    /** Показать экран выбора валюты */
    fun showCurrencyList(@IdRes containerId: Int, childFragmentManager: FragmentManager) {
        viewRouter.showCurrencyList(containerId, childFragmentManager) {
            when (selectedValue) {
                Value.CONVERTED_VALUE -> _unitPreview1.postValue(it)
                Value.RESULT_VALUE -> _unitPreview2.postValue(it)
            }
        }
    }

    /** Обработать нажатие на клавиатуру */
    fun clickKeypad(symbol: String) {
        addSymbolInField(symbol)
    }

    /** Удалить последний символ у выбронной единицы измерения */
    private fun deleteLastSymbol(unit: MutableLiveData<String>) {
        var currentValue = unit.value ?: return
        val end = if (currentValue.isEmpty()) 0 else currentValue.length - 1
        val result = if (end == 0) "0" else currentValue.substring(0, end)
        unit.postValue(result)
    }

    /** Добавить или удалить символ */
    private fun addSymbolInField(symbol: String) {
        if (symbol == "backspace") {
            deleteLastSymbol(getSelectedUnit())
        } else {
            addSymbol(getSelectedUnit(), symbol)
        }
    }

    /** Добавить символ для выбранной единицы измерения */
    private fun addSymbol(unit: MutableLiveData<String>, symbol: String) {
        var currentValue = unit.value ?: return
        if (symbol == "." && currentValue.contains(".")) return
        if (currentValue == "0" && symbol != ".") currentValue = ""
        unit.postValue(currentValue + symbol)
    }

    /** Получить LiveData выбранной единицы измерения */
    private fun getSelectedUnit(): MutableLiveData<String> =
        when (selectedValue) {
            Value.CONVERTED_VALUE -> _unit1
            Value.RESULT_VALUE -> _unit2
        }
}

enum class Value {
    CONVERTED_VALUE, RESULT_VALUE
}