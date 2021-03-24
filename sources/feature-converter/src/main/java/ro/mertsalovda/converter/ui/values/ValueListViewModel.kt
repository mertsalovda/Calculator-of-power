package ro.mertsalovda.converter.ui.values

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ro.mertsalovda.converter.repository.CurrencyRepository
import ro.mertsalovda.converter.repository.PhysicalValueRepository
import ro.mertsalovda.converter.ui.converter.Mode
import ru.mertsalovda.core_api.database.entity.Value
import java.lang.Exception

class ValueListViewModel(
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

    /**
     * Получить список валют соответствующих поисковому запросу query
     * @return список валют удовлетворяющих запрос query
     */
    fun getCountriesByQuery(): LiveData<List<Value>> {
        val result = MediatorLiveData<List<Value>>()

        // Функция в соответствии с запросом query
        val filterFun = {
            val queryStr = query.value
            val currency = valueList.value

            result.value = if (queryStr.isNullOrEmpty()) currency
            else currency!!.filter {
                (it.name + it.code).contains(queryStr, true)
            }
        }
        // Объединение LiveData в MediatorLiveData
        result.addSource(valueList) { filterFun.invoke() }
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
}