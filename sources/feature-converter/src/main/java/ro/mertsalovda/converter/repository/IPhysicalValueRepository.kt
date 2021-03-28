package ro.mertsalovda.converter.repository

import ro.mertsalovda.converter.ui.converter.Mode
import ru.mertsalovda.core_api.database.entity.Value

interface IPhysicalValueRepository {

    /**
     * Получение списка физичиских величин в зависимости от режима конвертора
     * @param mode  режим конвертора
     * @return      список физических величин [Value]
     */
    fun getPhysicalValueByMode(mode: Mode): List<Value>?

    /**
     * Конвертировать физическую величину
     * @param fromValue конвертируемое значение
     * @param fromUnit  единица измерения конвертируемого значения
     * @param toUnit    единица измерения результата конвертирования
     * @return          результат конвертации, если запрос выполнился успешно или null
     */
    suspend fun convert(fromValue: Float, fromUnit: String, toUnit: String): Float?
}
