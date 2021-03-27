package ro.mertsalovda.converter.ui.values

import androidx.annotation.MainThread
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ro.mertsalovda.converter.repository.CurrencyRepository
import ro.mertsalovda.converter.repository.PhysicalValueRepository
import ro.mertsalovda.converter.ui.converter.Mode
import ru.mertsalovda.core_api.database.entity.Value
import java.lang.Exception
import javax.inject.Inject

class ValueListViewModel @Inject constructor(
    private val repository: CurrencyRepository,
    private val physicalValueRepository: PhysicalValueRepository,
) : ViewModel() {

    private val valueList = MutableLiveData<List<Value>>(listOf())

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    // Поисковый запрос пользователей
    private val query = MutableLiveData<String>()
    // Код исключаемого значения
    private val codeFilter = MutableLiveData<String?>()

    /**
     * Получить список валют соответствующих поисковому запросу query и исключающий codeFilter
     * @return  список валют удовлетворяющих запрос query и codeFilter
     */
    fun getCountriesByQuery(): LiveData<List<Value>> {
        val result = MediatorLiveData<List<Value>>()

        // Функция в соответствии с запросом query и codeFilter
        val filterFun = {
            val queryStr = query.value
            val codeFilter = codeFilter.value
            val currency = valueList.value

            result.value = if (queryStr.isNullOrEmpty() && codeFilter.isNullOrEmpty()) {
                currency
            } else {
                currency!!.filter { value ->
                    val isQuery = if (queryStr != null) {
                        (value.name + value.code).contains(queryStr, true)
                    } else {
                        true
                    }
                    isQuery && value.code != codeFilter
                }
            }
        }
        // Объединение LiveData в MediatorLiveData
        result.addSource(valueList) { filterFun.invoke() }
        result.addSource(query) { filterFun.invoke() }
        result.addSource(codeFilter) { filterFun.invoke() }

        return result
    }

    /**
     * Установить фильтрующий запрос
     * @param query текст запроса
     */
    @MainThread
    fun setSearchQuery(query: String) {
        this.query.value = query
    }

    /** Загрузить список валюты */
    fun loadCurrencyList() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val result = repository.getCurrencyItemList()
                valueList.postValue(result)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    /**
     * Загрузить список физических величин
     * @param mode  режим конвертора [Mode]
     */
    fun loadPhysicalValueList(mode: Mode) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val result = physicalValueRepository.getPhysicalValueByMode(mode)
                valueList.postValue(result)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    /**
     * Установить значение фильтра
     * @param codeFilter    значение фильтра
     */
    @MainThread
    fun setFilter(codeFilter: String?) {
        this.codeFilter.value = codeFilter
    }
}