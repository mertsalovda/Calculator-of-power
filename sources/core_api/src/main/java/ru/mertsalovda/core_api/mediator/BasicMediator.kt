package ru.mertsalovda.core_api.mediator

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager

interface BasicMediator {

    fun openBasicScreen(@IdRes containerId: Int, fragmentManager: FragmentManager)
}