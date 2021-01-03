package ru.mertsalovda.core_api.mediator

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager

interface GraphMediator {

    fun openGraphScreen(@IdRes containerId: Int, fragmentManager: FragmentManager)
}