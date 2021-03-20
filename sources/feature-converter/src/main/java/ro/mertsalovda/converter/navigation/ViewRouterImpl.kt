package ro.mertsalovda.converter.navigation

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import ro.mertsalovda.converter.ui.converter.ConverterFragment
import ro.mertsalovda.converter.ui.converter.Mode
import ro.mertsalovda.converter.ui.currency.ValueListFragment
import ru.mertsalovda.core_api.database.entity.Value
import javax.inject.Inject

class ViewRouterImpl @Inject constructor() : ViewRouter {

    override fun showConverter(containerId: Int, childFragmentManager: FragmentManager) {
        childFragmentManager.beginTransaction()
            .add(containerId, ConverterFragment.newInstance(), ConverterFragment::class.simpleName)
            .commit()
    }

    override fun showCurrencyList(
        @IdRes containerId: Int,
        mode: Mode,
        childFragmentManager: FragmentManager,
        onValueSelected: ((Value) -> Unit)?
    ) {
        childFragmentManager.beginTransaction()
            .add(
                containerId,
                ValueListFragment.newInstance(mode, onValueSelected),
                ValueListFragment::class.simpleName
            )
            .addToBackStack(ValueListFragment::class.simpleName)
            .commit()
    }
}