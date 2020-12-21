package ru.mertsalovda.core_api.mediator

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager

interface ScientificCalculatorMediator {

    fun openScientificScreen(@IdRes containerId: Int, fragmentManager: FragmentManager)
}