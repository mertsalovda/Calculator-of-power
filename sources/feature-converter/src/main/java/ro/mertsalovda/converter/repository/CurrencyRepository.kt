package ro.mertsalovda.converter.repository

import kotlinx.coroutines.*
import ru.mertsalovda.core_api.dto.CurrencyItem
import ru.mertsalovda.core_api.database.CalculatorDao
import ru.mertsalovda.core_api.dto.Country
import ru.mertsalovda.core_api.network.CountriesApi
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

/**
 * Репозиторий для получения списка валют по странам.
 *
 * @property countriesApi   API страны
 * @property calculatorDao  База данных
 */
@Singleton
class CurrencyRepository @Inject constructor(
    private val countriesApi: CountriesApi,
    private val calculatorDao: CalculatorDao,
    ) : CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    /** Список поддерживаемых валют */
    private val currencyCodeList = listOf(
        "CAD", "HKD", "ISK", "PHP", "DKK", "HUF", "CZK", "AUD",
        "RON", "SEK", "IDR", "INR", "BRL", "RUB", "HRK", "JPY",
        "THB", "CHF", "SGD", "PLN", "BGN", "TRY", "CNY", "NOK",
        "NZD", "ZAR", "USD", "MXN", "ILS", "GBP", "KRW", "MYR",
        "EUR"
    )

    /** Загрузить список валюты из интернета*/
    private suspend fun getCurrencyListFromNetwork(): List<CurrencyItem> =
        withContext(coroutineContext) {
            val response = countriesApi.getAllCountries()
            if (response.isSuccessful) {
                val list = mapCountryListToCurrencyItemList(response.body()!!)
                if (list.isNotEmpty()) {
                    calculatorDao.insertCurrency(list)
                }
                list
            } else {
                listOf<CurrencyItem>()
            }
        }

    /** Загрузить список валюты из БД*/
    suspend fun getCurrencyItemList(): List<CurrencyItem> =
        withContext(coroutineContext) {
            val list = calculatorDao.getAllCurrencyItems()
            if (list.isNotEmpty()) {
                list
            } else {
                getCurrencyListFromNetwork()
            }
        }

    /**
     * Преобразовать список [Country] в список [CurrencyItem]
     * @param country список стран
     */
    private fun mapCountryListToCurrencyItemList(country: List<Country>): List<CurrencyItem> {
        return country.flatMap { country ->
            country.currencies.filter { currency ->
                !currency.code.isNullOrEmpty()
                        && currency.code != "(none)"
                        && currencyCodeList.contains(currency.code)
            }.map { currency ->
                CurrencyItem(
                    countryName = country.name,
                    currencyCode = currency.code!!,
                    flagUrl = country.flag
                )
            }
        }
    }
}