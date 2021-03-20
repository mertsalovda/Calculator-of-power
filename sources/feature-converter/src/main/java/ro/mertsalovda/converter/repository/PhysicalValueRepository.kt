package ro.mertsalovda.converter.repository

import kotlinx.coroutines.*
import ro.mertsalovda.converter.R
import ro.mertsalovda.converter.repository.mapper.CurrencyMapper
import ro.mertsalovda.converter.repository.mapper.CurrencyValueMapper
import ro.mertsalovda.converter.ui.converter.Mode
import ru.mertsalovda.core_api.database.entity.CurrencyItem
import ru.mertsalovda.core_api.database.dao.CurrencyDao
import ru.mertsalovda.core_api.database.entity.Value
import ru.mertsalovda.core_api.network.CountriesApi
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

/**
 * Репозиторий для получения списка физических величин.
 */
@Singleton
class PhysicalValueRepository @Inject constructor() : CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    private val map = hashMapOf<Mode, List<Value>>(
        Mode.SPEED to listOf<Value.Speed>(
            Value.Speed("километр в час", "км/ч", "${R.drawable.ic_speedometer}"),
            Value.Speed("метр в час", "м/ч", "${R.drawable.ic_speedometer}"),
            Value.Speed("километр в секунду", "км/с", "${R.drawable.ic_speedometer}"),
            Value.Speed("метр в секунду", "м/с", "${R.drawable.ic_speedometer}"),
        ),
        Mode.LENGTH to listOf<Value.Length>(
            Value.Length("Миллиметр", "мм", "${R.drawable.ic_length}"),
            Value.Length("Сантиметр", "см", "${R.drawable.ic_length}"),
            Value.Length("Дециметр", "дм", "${R.drawable.ic_length}"),
            Value.Length("Метр", "м", "${R.drawable.ic_length}"),
            Value.Length("Километр", "км", "${R.drawable.ic_length}"),
        ),
        Mode.AREA to listOf<Value.Area>(
            Value.Area("Миллиметр в квадрате", "мм^2", "${R.drawable.ic_area}"),
            Value.Area("Сантиметр в квадрате", "см^2", "${R.drawable.ic_area}"),
            Value.Area("Дециметр в квадрате", "дм^2", "${R.drawable.ic_area}"),
            Value.Area("Метр в квадрате", "м^2", "${R.drawable.ic_area}"),
            Value.Area("Километр в квадрате", "км^2", "${R.drawable.ic_area}"),
        ),
        Mode.WEIGHT to listOf<Value.Weight>(
            Value.Weight("Грамм", "г", "${R.drawable.ic_weighing_machine}"),
            Value.Weight("Килограмм", "кг", "${R.drawable.ic_weighing_machine}"),
            Value.Weight("Центнер", "ц", "${R.drawable.ic_weighing_machine}"),
            Value.Weight("Тонна", "т", "${R.drawable.ic_weighing_machine}"),
        ),
    )

    /**
     * Получение списка физичиских величин в зависимости от режима конвертора
     * @param mode  режим конвертора
     * @return      список физических величин [Value]
     */
    fun getPhysicalValueByMode(mode: Mode): List<Value>? = map[mode]
}