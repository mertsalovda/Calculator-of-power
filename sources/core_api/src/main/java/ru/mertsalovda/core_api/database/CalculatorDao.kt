package ru.mertsalovda.core_api.database

import androidx.room.*
import ru.mertsalovda.core_api.dto.CurrencyItem
import ru.mertsalovda.core_api.dto.exchange.ExchangeRate

@Dao
interface CalculatorDao {

    /**
     * Вставить список валют по странам
     * @param currencyItemList  список валют по странам
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrency(currencyItemList: List<CurrencyItem>)

    /**
     * Удалить информацию о валюте по странам
     * @param currencyItemList  список валют по странам
     */
    @Delete
    suspend fun deleteAllCurrencyItem(vararg currencyItemList: CurrencyItem)

    /**
     * Получить список валют по странам
     * @return список валют по странам
     */
    @Query("SELECT * FROM currency_item")
    suspend fun getAllCurrencyItems(): List<CurrencyItem>

    /**
     * Вставить в БД обменный курс
     * @param rate  обменный курс относительно базовой
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRate(rate: ExchangeRate)

    /**
     * Удалить обменный курс
     * @param rate  обменный курс
     */
    @Delete
    suspend fun deleteAllRate(vararg rate: ExchangeRate)

    /**
     * Получить из БД обменный курс относительно базовой валюты
     * @param base  код базовой валюты
     * @return      обменный курс
     */
    @Query("SELECT * FROM exchange_rate WHERE base = :base")
    suspend fun getExchangeRateByBaseCurrency(base: String): ExchangeRate?
}