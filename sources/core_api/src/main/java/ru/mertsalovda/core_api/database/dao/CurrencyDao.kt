package ru.mertsalovda.core_api.database.dao

import androidx.room.*
import ru.mertsalovda.core_api.database.entity.CurrencyItem

/**
 * DAO для работы с валютой.
 */
@Dao
interface CurrencyDao {

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
}