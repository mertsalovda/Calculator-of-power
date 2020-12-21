package ro.mertsalovda.converter.navigation

import androidx.fragment.app.FragmentManager
import ro.mertsalovda.converter.ui.currency.CurrencyListFragment
import javax.inject.Inject

class ViewRouterImpl @Inject constructor(): ViewRouter {

    override fun showCurrencyList(childFragmentManager: FragmentManager) {
        childFragmentManager.beginTransaction()
            .add(CurrencyListFragment.newInstance(), CurrencyListFragment::class.simpleName)
            .addToBackStack(CurrencyListFragment::class.simpleName)
            .commit()
    }
}