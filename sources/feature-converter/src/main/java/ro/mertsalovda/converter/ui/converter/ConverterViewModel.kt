package ro.mertsalovda.converter.ui.converter

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ro.mertsalovda.converter.navigation.ViewRouter
import ro.mertsalovda.converter.repository.CurrencyConverterRepository
import ru.mertsalovda.core_api.dto.CurrencyItem
import ru.mertsalovda.core_api.dto.exchange.ExchangeRate
import kotlin.math.round

class ConverterViewModel(
    private val viewRouter: ViewRouter,
    private val currencyConverterRepository: CurrencyConverterRepository
) : ViewModel() {

    /** Преобразуемая единица измерения Value.CONVERTED_VALUE*/
    private val _unit1 = MutableLiveData<String>("0")
    val unit1: LiveData<String> = _unit1

    /** Преобразованная единица измерения Value.RESULT_VALUE */
    private val _unit2 = MutableLiveData<String>("0")
    val unit2: LiveData<String> = _unit2

    /** Текущее выбранное значение */
    private var selectedValue = Value.CONVERTED_VALUE

    /** Представление для преобразуемой единици измерения */
    private val _unitPreview1 = MutableLiveData<CurrencyItem?>(null)
    val unitPreview1: LiveData<CurrencyItem?> = _unitPreview1

    /** Представление для преобразованной единици измерения */
    private val _unitPreview2 = MutableLiveData<CurrencyItem?>(null)
    val unitPreview2: LiveData<CurrencyItem?> = _unitPreview2

    /** Обменный курс валют */
    private val _exchangeRate = MutableLiveData<ExchangeRate?>(null)

    /** Указать какая величина в фокусе */
    fun setValueFocused(value: Value) {
        this.selectedValue = value
    }

    /** Показать экран выбора валюты */
    fun showCurrencyList(@IdRes containerId: Int, childFragmentManager: FragmentManager) {
        viewRouter.showCurrencyList(containerId, childFragmentManager) {
            when (selectedValue) {
                Value.CONVERTED_VALUE -> _unitPreview1.value = it
                Value.RESULT_VALUE -> _unitPreview2.value = it
            }
        }
    }

    /** Обработать нажатие на клавиатуру */
    fun clickKeypad(symbol: String) {
        addSymbolInField(symbol)
    }

    /** Удалить последний символ у выбронной единицы измерения */
    private fun deleteLastSymbol(unit: MutableLiveData<String>) {
        val currentValue = unit.value ?: return
        val end = if (currentValue.isEmpty()) 0 else currentValue.length - 1
        val result = if (end == 0) "0" else currentValue.substring(0, end)
        unit.value = result
    }

    /** Добавить или удалить символ */
    private fun addSymbolInField(symbol: String) {
        if (symbol == "backspace") {
            deleteLastSymbol(getSelectedUnit())
        } else {
            addSymbol(getSelectedUnit(), symbol)
        }
        when(selectedValue) {
            Value.CONVERTED_VALUE -> calculate(_unit1, _unit2)
            Value.RESULT_VALUE -> calculate(_unit2, _unit1, true)
        }
    }

    /** Добавить символ для выбранной единицы измерения */
    private fun addSymbol(unit: MutableLiveData<String>, symbol: String) {
        var currentValue = unit.value ?: return
        if (symbol == "." && currentValue.contains(".")) return
        if (currentValue == "0" && symbol != ".") currentValue = ""
        unit.value = currentValue + symbol
    }

    /** Получить LiveData выбранной единицы измерения */
    private fun getSelectedUnit(): MutableLiveData<String> =
        when (selectedValue) {
            Value.CONVERTED_VALUE -> _unit1
            Value.RESULT_VALUE -> _unit2
        }

    /** Загрузить обменный курс валют */
    fun loadExchangeRateForBaseCurrency() {
        viewModelScope.launch {
            _unitPreview1.value?.currencyCode?.let { code ->
                try {
                    val exchangeRate = currencyConverterRepository.getExchangeRateByBaseCurrency(code)
                    _exchangeRate.postValue(exchangeRate)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * Рассчитать конвертацию
     *
     * @param sours
     * @param target
     * @param isRevert
     */
    private fun calculate(
        sours: MutableLiveData<String>,
        target: MutableLiveData<String>,
        isRevert: Boolean = false
    ) {
        if (_unitPreview2.value != null) {
            val targetCode = _unitPreview2.value?.currencyCode
            val rate = _exchangeRate.value?.rates?.get(targetCode) ?: 0.0
            val result = if (isRevert) {
                sours.value?.let { it.toDouble() / rate } ?: 0.0
            } else {
                sours.value?.let { it.toDouble() * rate } ?: 0.0
            }
            target.value = (round(result * 100) / 100).toString()
        }
    }
}

enum class Value {
    CONVERTED_VALUE, RESULT_VALUE
}