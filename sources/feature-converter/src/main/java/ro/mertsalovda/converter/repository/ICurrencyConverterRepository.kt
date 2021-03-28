package ro.mertsalovda.converter.repository

import ru.mertsalovda.core_api.database.entity.ExchangeRate

/** Интерфейс репозитория для получения данных кухрса обмена валют*/
interface ICurrencyConverterRepository {

    /**
     * Загрузить обменный курс валют.
     * @param base  базованя валюта, относительно которой будет делаться запрос.
     *
     * @return      обменный курс [ExchangeRate] валют относительно базоваой
     */
    suspend fun getExchangeRateByBaseCurrency(base: String): ExchangeRate?
}