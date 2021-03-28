package ro.mertsalovda.converter.ui.converter

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ro.mertsalovda.converter.navigation.IViewRouter
import ro.mertsalovda.converter.repository.ICurrencyConverterRepository
import ro.mertsalovda.converter.repository.IPhysicalValueRepository
import ru.mertsalovda.core_api.database.entity.ExchangeRate
import ru.mertsalovda.core_api.database.entity.Value
import javax.inject.Inject
import kotlin.math.round

/**
 * ViewModel экрана конвертора.
 * @property viewRouter                     класс навигации.
 * @property currencyConverterRepository    репозиторий валют.
 * @property physicalValueRepository        репозиторий физических величин.
 */
class ConverterViewModel @Inject constructor(
    private val viewRouter: IViewRouter,
    private val currencyConverterRepository: ICurrencyConverterRepository,
    private val physicalValueRepository: IPhysicalValueRepository,
) : ViewModel() {

    /** Режим конвертора. По умолчанию обмен валют */
    private var mode: Mode = Mode.CURRENCY

    /** Преобразуемая единица измерения Value.CONVERTED_VALUE*/
    private val _unit1 = MutableLiveData<String>("0")
    val unit1: LiveData<String> = _unit1

    /** Преобразованная единица измерения Value.RESULT_VALUE */
    private val _unit2 = MutableLiveData<String>("0")
    val unit2: LiveData<String> = _unit2

    /** Текущее выбранное значение */
    private var selectedValue = ConverterValue.CONVERTED_VALUE

    /** Представление для преобразуемой единици измерения */
    private val _unitPreview1 = MutableLiveData<Value?>(null)
    val unitPreview1: LiveData<Value?> = _unitPreview1

    /** Представление для преобразованной единици измерения */
    private val _unitPreview2 = MutableLiveData<Value?>(null)
    val unitPreview2: LiveData<Value?> = _unitPreview2

    /** Обменный курс валют */
    private var exchangeRate: ExchangeRate? = null

    /** Коэффициент преобразования физической величины */
    private var conversionFactor: Float = 0f

    /** Указать какая величина в фокусе */
    fun setValueFocused(converterValue: ConverterValue) {
        this.selectedValue = converterValue
    }

    /** Показать экран выбора валюты или физической величины */
    fun showValueList(@IdRes containerId: Int, childFragmentManager: FragmentManager) {
        val codeFilter = getCodeFilter()
        viewRouter.showValueList(containerId, mode, childFragmentManager, codeFilter) {
            when (selectedValue) {
                ConverterValue.CONVERTED_VALUE -> _unitPreview1.value = it
                ConverterValue.RESULT_VALUE -> _unitPreview2.value = it
            }
        }
    }

    /**
     * Получить значение фильтра по коду.
     * @return  код противоположной единицы измерения.
     */
    private fun getCodeFilter(): String? =
        when (selectedValue) {
            ConverterValue.CONVERTED_VALUE -> _unitPreview2.value?.code
            ConverterValue.RESULT_VALUE -> _unitPreview1.value?.code
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
        when (selectedValue) {
            ConverterValue.CONVERTED_VALUE -> calculate(_unit1, _unit2)
            ConverterValue.RESULT_VALUE -> calculate(_unit2, _unit1, true)
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
            ConverterValue.CONVERTED_VALUE -> _unit1
            ConverterValue.RESULT_VALUE -> _unit2
        }

    /** Загрузить коэффициент преобразования */
    fun loadCoefficient() {
        when (mode) {
            Mode.CURRENCY -> loadExchangeRateForBaseCurrency()
            else -> loadConversionFactor()
        }
    }

    /** Загрузить обменный курс валют */
    private fun loadExchangeRateForBaseCurrency() {
        viewModelScope.launch {
            _unitPreview1.value?.code?.let { code ->
                try {
                    val exchangeRate =
                        currencyConverterRepository.getExchangeRateByBaseCurrency(code)
                    this@ConverterViewModel.exchangeRate = exchangeRate
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    /** Загрузить коэффициент преобразования физической величины*/
    private fun loadConversionFactor() {
        viewModelScope.launch {
            val fromValue = 1f
            val fromUnit = unitPreview1.value?.unit ?: return@launch
            val toUnit = unitPreview2.value?.unit ?: return@launch
            try {
                conversionFactor = physicalValueRepository.convert(fromValue, fromUnit, toUnit) ?: 0f
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Рассчитать конвертацию
     *
     * @param sours     конвертируемое значение
     * @param target    результат
     * @param isRevert  направление конвертации
     */
    private fun calculate(
        sours: MutableLiveData<String>,
        target: MutableLiveData<String>,
        isRevert: Boolean = false
    ) {
        if (_unitPreview2.value != null) {
            val targetCode = _unitPreview2.value?.code
            val rate = getRate(targetCode)
            val result = if (isRevert) {
                sours.value?.let { it.toDouble() / rate } ?: 0.0
            } else {
                sours.value?.let { it.toDouble() * rate } ?: 0.0
            }
            val targetResult = round(result * 100) / 100
            target.value = if (targetResult != 0.0) targetResult.toString() else "0"
        }
    }

    /**
     * Получить коэффициент преобразования
     * @param targetCode    код результирующей величины
     * @return              коэффициент преобразования [Double]
     *                      или 0.0 если коэффициент не найден
     */
    private fun getRate(targetCode: String?): Double {
        return when(mode) {
            Mode.CURRENCY -> exchangeRate?.rates?.get(targetCode) ?: 0.0
            else -> conversionFactor.toDouble()
        }
    }

    /**
     * Установить режим конвертора
     * @param mode  режим конвертора [Mode]
     */
    fun setMode(mode: Mode) {
        clearConverterValue()
        this.mode = mode
    }

    /** Очистить данные */
    private fun clearConverterValue() {
        _unitPreview1.postValue(null)
        _unitPreview2.postValue(null)
        _unit1.postValue("0")
        _unit2.postValue("0")
        conversionFactor = 0f
    }
}

enum class ConverterValue {
    CONVERTED_VALUE, RESULT_VALUE
}

/**
 * Режимы конвертора
 */
enum class Mode {
    CURRENCY, LENGTH, WEIGHT, SPEED, AREA,
}