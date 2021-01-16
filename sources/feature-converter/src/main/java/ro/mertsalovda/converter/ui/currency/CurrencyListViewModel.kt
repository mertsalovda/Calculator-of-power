package ro.mertsalovda.converter.ui.currency

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.mertsalovda.core.CoreProvidersFactory
import ru.mertsalovda.core_api.dto.Country
import ru.mertsalovda.core_api.network.CountriesApi
import java.lang.Exception

class CurrencyListViewModel : ViewModel() {

    private var countriesApi: CountriesApi =
        CoreProvidersFactory.createNetworkBuilder().provideCountriesApi()

    private val currencyList = MutableLiveData<List<CurrencyItem>>(listOf())

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    // Поисковый запрос пользователей
    private val query = MutableLiveData<String>()

    /**
     * Получить список стран соответствующих поисковому запросу query
     * @return список стран удовлетворяющих запрос query
     */
    fun getCountriesByQuery(): LiveData<List<CurrencyItem>> {
        val result = MediatorLiveData<List<CurrencyItem>>()

        // Функция фильтрации стран в соответствии с запросом query
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

    private fun mapCountryListToCurrencyItemList(country: List<Country>) {
        val result = country.flatMap { country ->
            country.currencies.filter {
                !it.code.isNullOrEmpty() && it.code != "(none)"
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