package ro.mertsalovda.converter.navigation

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import ro.mertsalovda.converter.ui.converter.ConverterFragment
import ro.mertsalovda.converter.ui.converter.Mode
import ro.mertsalovda.converter.ui.values.ValueListFragment
import ru.mertsalovda.core_api.database.entity.Value
import javax.inject.Inject

class ViewRouterImpl @Inject constructor() : IViewRouter {

    override fun showConverter(containerId: Int, childFragmentManager: FragmentManager) {
        childFragmentManager.beginTransaction()
            .replace(containerId, ConverterFragment.newInstance(), ConverterFragment::class.simpleName)
            .commit()
    }

    override fun showValueList(
        @IdRes containerId: Int,
        mode: Mode,
        childFragmentManager: FragmentManager,
        codeFilter: String?,
        onValueSelected: ((Value) -> Unit)?
    ) {
        childFragmentManager.beginTransaction()
            .replace(
                containerId,
                ValueListFragment.newInstance(mode,  codeFilter, onValueSelected),
                ValueListFragment::class.simpleName
            )
            .addToBackStack(ValueListFragment::class.simpleName)
            .commit()
    }
}