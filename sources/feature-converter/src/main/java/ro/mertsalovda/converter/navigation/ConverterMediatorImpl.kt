package ro.mertsalovda.converter.navigation

import androidx.fragment.app.FragmentManager
import ro.mertsalovda.converter.ConverterFlowFragment
import ru.mertsalovda.core_api.mediator.ConverterMediator
import javax.inject.Inject

class ConverterMediatorImpl @Inject constructor(): ConverterMediator {

    override fun openConverterScreen(containerId: Int, fragmentManager: FragmentManager) {
        fragmentManager.beginTransaction()
            .add(containerId, ConverterFlowFragment.newInstance())
            .commit()
    }
}