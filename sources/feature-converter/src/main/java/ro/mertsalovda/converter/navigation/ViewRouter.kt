package ro.mertsalovda.converter.navigation

import androidx.fragment.app.FragmentManager

interface ViewRouter {

    fun showCurrencyList(childFragmentManager: FragmentManager)
}