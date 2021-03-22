package ro.mertsalovda.converter.navigation

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import ro.mertsalovda.converter.ui.converter.Mode
import ru.mertsalovda.core_api.database.entity.Value

interface ViewRouter {

    /** Открыть экран Конвертор */
    fun showConverter(@IdRes containerId: Int, childFragmentManager: FragmentManager)

    /**
     * Показать экран выбора единиц измерения
     * @param onValueSelected    callback из которого можно получить выбранную единицу измерения.
     */
    fun showCurrencyList(
        @IdRes containerId: Int,
        mode: Mode,
        childFragmentManager: FragmentManager,
        onValueSelected: ((Value) -> Unit)?
    )
}