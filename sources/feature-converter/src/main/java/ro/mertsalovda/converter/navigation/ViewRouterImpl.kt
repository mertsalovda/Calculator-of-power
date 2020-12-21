package ro.mertsalovda.converter.navigation

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import ro.mertsalovda.converter.ui.currency.CurrencyListFragment
import javax.inject.Inject

class ViewRouterImpl @Inject constructor(): ViewRouter {

    override fun showCurrencyList(@IdRes containerId: Int, fragmentManager: FragmentManager) {
        fragmentManager.beginTransaction()
            .add(CurrencyListFragment.newInstance(), null)
            .addToBackStack(CurrencyListFragment::class.java.simpleName)
            .commit()
    }
}