package ru.mertsalovda.core_api.mediator

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager

interface BasicCalculatorMediator {

    fun openBasicScreen(@IdRes containerId: Int, fragmentManager: FragmentManager)
}