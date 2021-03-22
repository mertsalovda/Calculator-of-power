package ro.mertsalovda.converter.repository.mapper

import ru.mertsalovda.core_api.database.entity.CurrencyItem
import ru.mertsalovda.core_api.database.entity.Value
import javax.inject.Inject

/**
 * Класс преобразовывает [CurrencyItem] в [Value.Currency]
 */
class CurrencyValueMapper @Inject constructor() {

    /**
     * Преобразовать [CurrencyItem] в [Value.Currency]
     */
    fun map(currencyItem: CurrencyItem): Value.Currency {
        return Value.Currency(
            currencyItem.countryName,
            currencyItem.currencyCode,
            currencyItem.flagUrl,
            ""
        )
    }
}