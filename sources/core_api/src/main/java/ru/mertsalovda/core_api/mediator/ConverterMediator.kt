package ru.mertsalovda.core_api.mediator

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager

interface ConverterMediator {

    fun openConverterScreen(@IdRes containerId: Int, fragmentManager: FragmentManager)
}