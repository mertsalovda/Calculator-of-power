package ro.mertsalovda.converter.navigation

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager

interface ViewRouter {

    fun showCurrencyList(@IdRes containerId: Int, fragmentManager: FragmentManager)
}