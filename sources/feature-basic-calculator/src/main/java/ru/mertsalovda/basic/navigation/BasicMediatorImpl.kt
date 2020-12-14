package ru.mertsalovda.basic.navigation

import androidx.fragment.app.FragmentManager
import ru.mertsalovda.basic.BasicFragment
import ru.mertsalovda.core_api.mediator.BasicMediator
import javax.inject.Inject

class BasicMediatorImpl @Inject constructor() : BasicMediator {

    override fun openBasicScreen(containerId: Int, fragmentManager: FragmentManager) {
        fragmentManager.beginTransaction()
            .replace(containerId, BasicFragment.newInstance())
            .commit()
    }
}