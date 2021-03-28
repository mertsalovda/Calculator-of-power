package ro.mertsalovda.converter.repository

import kotlinx.coroutines.*
import ro.mertsalovda.converter.R
import ro.mertsalovda.converter.ui.converter.Mode
import ru.mertsalovda.core_api.database.entity.Value
import ru.mertsalovda.core_api.network.ConverterApi
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

/**
 * Репозиторий для получения списка физических величин.
 * @param converterApi  API для получения конвертируемой величины.
 */
@Singleton
class PhysicalValueRepository @Inject constructor(
    private val converterApi: ConverterApi,
) : IPhysicalValueRepository, CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    private val map = hashMapOf<Mode, List<Value>>(
        Mode.SPEED to listOf<Value.Speed>(
            Value.Speed("километр в час", "км/ч", "${R.drawable.ic_speedometer}", "kilometer-per-hour"),
            Value.Speed("километр в секунду", "км/с", "${R.drawable.ic_speedometer}", "kilometer-per-second"),
            Value.Speed("метр в секунду", "м/с", "${R.drawable.ic_speedometer}", "meter-per-second"),
            Value.Speed("Морской узел", "kn", "${R.drawable.ic_speedometer}", "knot"),
            Value.Speed("Мах", "Mach", "${R.drawable.ic_speedometer}", "mach"),
            Value.Speed("Миль в час", "ml/h", "${R.drawable.ic_speedometer}", "mile-per-hour"),
            Value.Speed("Фут в секунду", "ft/s", "${R.drawable.ic_speedometer}", "foot-per-second"),
        ),
        Mode.LENGTH to listOf<Value.Length>(
            Value.Length("Миллиметр", "мм", "${R.drawable.ic_length}", "millimeter"),
            Value.Length("Сантиметр", "см", "${R.drawable.ic_length}", "centimeter"),
            Value.Length("Метр", "м", "${R.drawable.ic_length}", "meter"),
            Value.Length("Километр", "км", "${R.drawable.ic_length}", "kilometer"),
            Value.Length("Миля", "ml", "${R.drawable.ic_length}", "mile"),
            Value.Length("Фут", "ft", "${R.drawable.ic_length}", "foot"),
            Value.Length("Дюйм", "in", "${R.drawable.ic_length}", "inch"),
            Value.Length("Ярд", "yd", "${R.drawable.ic_length}", "yard"),
            Value.Length("Нанометр", "нм", "${R.drawable.ic_length}", "nanometer"),
            Value.Length("Микрометр", "мкм", "${R.drawable.ic_length}", "micrometer"),
        ),
        Mode.AREA to listOf<Value.Area>(
            Value.Area("Квадратный миллиметр", "мм^2", "${R.drawable.ic_area}", "square-millimeter"),
            Value.Area("Квадратный сантиметр", "см^2", "${R.drawable.ic_area}", "square-centimeter"),
            Value.Area("Квадратный метр", "м^2", "${R.drawable.ic_area}", "square-meter"),
            Value.Area("Квадратный километр", "км^2", "${R.drawable.ic_area}", "square-kilometer"),
            Value.Area("Квадратная миля", "ml^2", "${R.drawable.ic_area}", "square-mile"),
            Value.Area("Квадратный фут", "ft^2", "${R.drawable.ic_area}", "square-foot"),
            Value.Area("Квадратный дюйм", "in^2", "${R.drawable.ic_area}", "square-inch"),
            Value.Area("Квадратный ярд", "yd^2", "${R.drawable.ic_area}", "square-yard"),
            Value.Area("Гектар", "га", "${R.drawable.ic_area}", "hectare"),
        ),
        Mode.WEIGHT to listOf<Value.Weight>(
            Value.Weight("Грамм", "г", "${R.drawable.ic_weighing_machine}", "gram"),
            Value.Weight("Килограмм", "кг", "${R.drawable.ic_weighing_machine}", "kilogram"),
            Value.Weight("Тонна", "т", "${R.drawable.ic_weighing_machine}", "metric-ton"),
            Value.Weight("Пуд", "lb", "${R.drawable.ic_weighing_machine}", "metric-ton"),
            Value.Weight("Унция", "oz", "${R.drawable.ic_weighing_machine}", "ounce"),
            Value.Weight("Миллиграмм", "мг", "${R.drawable.ic_weighing_machine}", "milligram"),
            Value.Weight("Карат", "ct", "${R.drawable.ic_weighing_machine}", "carat"),
        ),
    )

    /**
     * Получение списка физичиских величин в зависимости от режима конвертора
     * @param mode  режим конвертора
     * @return      список физических величин [Value]
     */
    override fun getPhysicalValueByMode(mode: Mode): List<Value>? = map[mode]

    /**
     * Конвертировать физическую величину
     * @param fromValue конвертируемое значение
     * @param fromUnit  единица измерения конвертируемого значения
     * @param toUnit    единица измерения результата конвертирования
     * @return          результат конвертации, если запрос выполнился успешно или null
     */
    override suspend fun convert(fromValue: Float, fromUnit: String, toUnit: String): Float? =
        withContext(coroutineContext) {
            val result = converterApi.convert(fromValue, fromUnit, toUnit)
            if (result.isSuccessful) {
                result.body()?.result?.floatValue
            } else {
                null
            }
        }
}