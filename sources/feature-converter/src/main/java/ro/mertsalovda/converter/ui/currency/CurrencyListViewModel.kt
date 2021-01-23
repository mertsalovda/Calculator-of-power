package ro.mertsalovda.converter.ui.currency

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ro.mertsalovda.converter.di.ConverterComponent
import ru.mertsalovda.core_api.dto.Country
import ru.mertsalovda.core_api.network.CountriesApi
import java.lang.Exception
import javax.inject.Inject

class CurrencyListViewModel : ViewModel() {

    /** Список поддерживаемых валют */
    private val currencyCodeList = listOf<String>(
        "CAD", "HKD", "ISK", "PHP", "DKK", "HUF", "CZK",
        "AUD", "RON", "SEK", "IDR", "INR", "BRL", "RUB",
        "HRK", "JPY", "THB", "CHF", "SGD", "PLN", "BGN",
        "TRY", "CNY", "NOK", "NZD", "ZAR", "USD", "MXN",
        "ILS", "GBP", "KRW", "MYR",
    )

    @Inject
    lateinit var countriesApi: CountriesApi

    private val currencyList = MutableLiveData<List<CurrencyItem>>(listOf())

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    // Поисковый запрос пользователей
    private val query = MutableLiveData<String>()

    init {
        ConverterComponent.create().inject(this)
    }

    /**
     * Получить список валют соответствующих поисковому запросу query
     * @return список валют удовлетворяющих запрос query
     */
    fun getCountriesByQuery(): LiveData<List<CurrencyItem>> {
        val result = MediatorLiveData<List<CurrencyItem>>()

        // Функция в соответствии с запросом query
        val filterFun = {
            val queryStr = query.value
            val currency = currencyList.value

            result.value = if (queryStr.isNullOrEmpty()) currency
            else currency!!.filter {
                (it.countryName + it.currencyCode).contains(queryStr, true)
            }
        }
        // Объединение LiveData в MediatorLiveData
        result.addSource(currencyList) { filterFun.invoke() }
        result.addSource(query) { filterFun.invoke() }

        return result
    }

    fun setSearchQuery(query: String) {
        this.query.value = query
    }

    /** Загрузить список валюты */
    fun loadCurrencyList() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val country = countriesApi.getAllCountries()
                if (country.isSuccessful) {
                    country.body()?.let { mapCountryListToCurrencyItemList(it) }
                } else {
                    _errorMessage.postValue(country.message())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    /**
     * Преобразовать список [Country] в список [CurrencyItem]
     * @param country список стран
     */
    private fun mapCountryListToCurrencyItemList(country: List<Country>) {
        val result = country.flatMap { country ->
            country.currencies.filter { currency ->
                !currency.code.isNullOrEmpty()
                        && currency.code != "(none)"
                        && currencyCodeList.contains(currency.code)
            }.map { currency ->
                CurrencyItem(
                    countryName = country.name,
                    currencyCode = currency.code,
                    flagUrl = country.flag
                )
            }
        }
        currencyList.postValue(result)
    }
}