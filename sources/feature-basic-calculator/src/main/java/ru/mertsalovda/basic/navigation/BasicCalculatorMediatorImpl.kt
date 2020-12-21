package ru.mertsalovda.basic.navigation

import androidx.fragment.app.FragmentManager
import ru.mertsalovda.basic.BasicCalculatorFragment
import ru.mertsalovda.core_api.mediator.BasicCalculatorMediator
import javax.inject.Inject

class BasicCalculatorMediatorImpl @Inject constructor() : BasicCalculatorMediator {

    override fun openBasicScreen(containerId: Int, fragmentManager: FragmentManager) {
        fragmentManager.beginTransaction()
            .replace(containerId, BasicCalculatorFragment.newInstance())
            .commit()
    }
}