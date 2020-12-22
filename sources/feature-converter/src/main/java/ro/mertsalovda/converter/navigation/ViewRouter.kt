package ro.mertsalovda.converter.navigation

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager

interface ViewRouter {

    fun showConverter(@IdRes containerId: Int, childFragmentManager: FragmentManager)
    fun showCurrencyList(@IdRes containerId: Int, childFragmentManager: FragmentManager)
}