package ro.mertsalovda.converter.repository

import android.annotation.SuppressLint
import kotlinx.coroutines.*
import ro.mertsalovda.converter.repository.mapper.ExchangeRateMapper
import ru.mertsalovda.core_api.database.dao.ExchangeRateDao
import ru.mertsalovda.core_api.database.entity.ExchangeRate
import ru.mertsalovda.core_api.dto.ExchangeRateDto
import ru.mertsalovda.core_api.network.ExchangeRatesApi
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

/**
 * Репозиторий для получения данных кухрса обмена валют
 *
 * @property exchangeRatesApi   API курса обмена валют
 * @property exchangeRateDao    База данных
 * @property exchangeRateMapper Преобразует тип [ExchangeRateDto] в [ExchangeRate] и обратно
 */
@Singleton
class CurrencyConverterRepository @Inject constructor(
    private val exchangeRatesApi: ExchangeRatesApi,
    private val exchangeRateDao: ExchangeRateDao,
    private val exchangeRateMapper: ExchangeRateMapper
) : ICurrencyConverterRepository, CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    override suspend fun getExchangeRateByBaseCurrency(base: String): ExchangeRate? =
        withContext(coroutineContext) {
            val exchangeRate = exchangeRateDao.getExchangeRateByBaseCurrency(base)
            return@withContext if (isValid(exchangeRate)) {
                exchangeRate
            } else {
                val exchangeRateDto = getExchangeRateByBaseCurrencyFromNetwork(base)
                exchangeRateMapper.reverseMap(exchangeRateDto)
            }
        }

    /**
     * Проверка валидности курса валют
     * @param rate курс валют
     * @return TRUE если не NULL и текущаяя дата соответствует дате сохраннённой в БД,
     *         FALSE если NULL или несвежая дата.
     */
    @SuppressLint("SimpleDateFormat")
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
    private suspend fun getExchangeRateByBaseCurrencyFromNetwork(base: String): ExchangeRateDto? =
        withContext(coroutineContext) {
            val response = exchangeRatesApi.getLatestByBaseCurrency(base)
            if (response.isSuccessful) {
                val rate = response.body()
                if (rate != null) {
                    exchangeRateDao.insertRate(exchangeRateMapper.reverseMap(rate)!!)
                }
                rate
            } else {
                null
            }
        }
}