package ro.mertsalovda.converter.ui.currency

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ro.mertsalovda.converter.repository.CurrencyRepository
import ru.mertsalovda.core_api.dto.Country
import ru.mertsalovda.core_api.dto.CurrencyItem
import java.lang.Exception

class CurrencyListViewModel(private val repository: CurrencyRepository) : ViewModel() {

    private val currencyList = MutableLiveData<List<CurrencyItem>>(listOf())

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    // Поисковый запрос пользователей
    private val query = MutableLiveData<String>()

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
                val result = repository.getCurrencyItemList()
                currencyList.postValue(result)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}