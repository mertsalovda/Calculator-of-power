package ro.mertsalovda.converter.repository.mapper

import ru.mertsalovda.core_api.database.entity.CurrencyItem
import ru.mertsalovda.core_api.dto.CountryDto
import javax.inject.Inject

/**
 * Преобразователь типа [CountryDto] в [CurrencyItem]
 */
class CurrencyMapper @Inject constructor() {

    /** Список поддерживаемых валют */
    private val currencyCodeList = listOf(
        "CAD", "HKD", "ISK", "PHP", "DKK", "HUF", "CZK", "AUD",
        "RON", "SEK", "IDR", "INR", "BRL", "RUB", "HRK", "JPY",
        "THB", "CHF", "SGD", "PLN", "BGN", "TRY", "CNY", "NOK",
        "NZD", "ZAR", "USD", "MXN", "ILS", "GBP", "KRW", "MYR",
        "EUR"
    )

    /**
     * Преобразовать список [CountryDto] в список [CurrencyItem]
     * @param countries список стран
     * @return          список [CurrencyItem]
     */
    fun map(countries: List<CountryDto>): List<CurrencyItem> {
        return countries.flatMap { country ->
            country.currencies.filter { currency ->
                !currency.code.isNullOrEmpty()
                        && currency.code != "(none)"
                        && currencyCodeList.contains(currency.code)
            }.map { currency ->
                CurrencyItem(
                    countryName = country.name,
                    currencyCode = currency.code!!,
                    flagUrl = country.flag
                )
            }
        }
    }
}