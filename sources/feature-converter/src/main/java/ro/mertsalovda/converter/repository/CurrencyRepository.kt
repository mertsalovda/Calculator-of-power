package ro.mertsalovda.converter.repository

import kotlinx.coroutines.*
import ro.mertsalovda.converter.repository.mapper.CurrencyMapper
import ru.mertsalovda.core_api.database.entity.CurrencyItem
import ru.mertsalovda.core_api.database.dao.CurrencyDao
import ru.mertsalovda.core_api.network.CountriesApi
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

/**
 * Репозиторий для получения списка валют по странам.
 *
 * @property countriesApi   API страны
 * @property currencyDao    База данных
 */
@Singleton
class CurrencyRepository @Inject constructor(
    private val countriesApi: CountriesApi,
    private val currencyDao: CurrencyDao,
    private val currencyMapper: CurrencyMapper,
    ) : CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    /** Загрузить список валюты из интернета*/
    private suspend fun getCurrencyListFromNetwork(): List<CurrencyItem> =
        withContext(coroutineContext) {
            val response = countriesApi.getAllCountries()
            if (response.isSuccessful) {
                val list = currencyMapper.map(response.body()!!)
                if (list.isNotEmpty()) {
                    currencyDao.insertCurrency(list)
                }
                list
            } else {
                listOf<CurrencyItem>()
            }
        }

    /** Загрузить список валюты из БД*/
    suspend fun getCurrencyItemList(): List<CurrencyItem> =
        withContext(coroutineContext) {
            val list = currencyDao.getAllCurrencyItems()
            if (list.isNotEmpty()) {
                list
            } else {
                getCurrencyListFromNetwork()
            }
        }
}