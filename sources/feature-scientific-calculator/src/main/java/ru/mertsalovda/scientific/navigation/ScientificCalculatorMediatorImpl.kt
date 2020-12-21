package ru.mertsalovda.scientific.navigation

import androidx.fragment.app.FragmentManager
import ru.mertsalovda.core_api.mediator.ScientificCalculatorMediator
import ru.mertsalovda.scientific.ScientificCalculatorFragment
import javax.inject.Inject

class ScientificCalculatorMediatorImpl @Inject constructor(): ScientificCalculatorMediator {
    override fun openScientificScreen(containerId: Int, fragmentManager: FragmentManager) {
        fragmentManager.beginTransaction()
            .replace(containerId, ScientificCalculatorFragment.newInstance())
            .commit()
    }
}