package ro.mertsalovda.converter.ui.currency

/**
 * Модель для отображения списка валюты в RecyclerView
 *
 * @property countryName назавание страны
 * @property currencyCode символ валюты
 * @property flagUrl флаг страны
 */
data class CurrencyItem(
    val countryName: String? = "",
    val currencyCode: String? = "",
    val flagUrl: String? = "",
)
