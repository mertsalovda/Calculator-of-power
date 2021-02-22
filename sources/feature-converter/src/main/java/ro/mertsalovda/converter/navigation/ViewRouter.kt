package ro.mertsalovda.converter.navigation

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import ru.mertsalovda.core_api.database.entity.CurrencyItem

interface ViewRouter {

    /** Открыть экран Конвертор */
    fun showConverter(@IdRes containerId: Int, childFragmentManager: FragmentManager)

    /**
     * Показать экран выбора валюты
     * @param onCurrencySelected    callback из которого можно получить выбранную валюту.
     */
    fun showCurrencyList(
        @IdRes containerId: Int,
        childFragmentManager: FragmentManager,
        onCurrencySelected: ((CurrencyItem) -> Unit)?
    )
}