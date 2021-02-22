package ru.mertsalovda.core_api.database.dao

import androidx.room.*
import ru.mertsalovda.core_api.database.entity.ExchangeRate

/**
 * DAO для работы с обменным курсом валют.
 */
@Dao
interface ExchangeRateDao {

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