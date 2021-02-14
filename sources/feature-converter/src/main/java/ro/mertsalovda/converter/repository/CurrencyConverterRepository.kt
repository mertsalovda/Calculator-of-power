package ro.mertsalovda.converter.repository

import kotlinx.coroutines.*
import ru.mertsalovda.core_api.database.CalculatorDao
import ru.mertsalovda.core_api.dto.exchange.ExchangeRate
import ru.mertsalovda.core_api.network.ExchangeRatesApi
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

/**
 * Репозиторий для получения данных кухрса обмена валют
 *
 * @property exchangeRatesApi API курса обмена валют
 * @property calculatorDao    База данных
 */
@Singleton
class CurrencyConverterRepository @Inject constructor(
    private val exchangeRatesApi: ExchangeRatesApi,
    private val calculatorDao: CalculatorDao,
) : CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO



    /** Загрузить обменный курс валют */
    suspend fun getExchangeRateByBaseCurrency(base: String): ExchangeRate? =
        withContext(coroutineContext) {
            val exchangeRate = calculatorDao.getExchangeRateByBaseCurrency(base)
            return@withContext if (isValid(exchangeRate)) {
                exchangeRate
            } else {
                getExchangeRateByBaseCurrencyFromNetwork(base)
            }
        }

    /**
     * Проверка валидности курса валют
     * @param rate курс валют
     * @return TRUE если не NULL и текущаяя дата соответствует дате сохраннённой в БД,
     *         FALSE если NULL или несвежая дата.
     */
    private fun isValid(rate: ExchangeRate?): Boolean {
        var result = false
        rate?.let {
            val date = Date()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            result = dateFormat.format(date).toString() == it.date
        }
        return result
    }

    /** Загрузить обменный курс валют из интернета*/
    private suspend fun getExchangeRateByBaseCurrencyFromNetwork(base: String): ExchangeRate? =
        withContext(coroutineContext) {
            val response = exchangeRatesApi.getLatestByBaseCurrency(base)
            if (response.isSuccessful) {
                val rate = response.body()
                if (rate != null) {
                    calculatorDao.insertRate(rate)
                }
                rate
            } else {
                null
            }
        }
}