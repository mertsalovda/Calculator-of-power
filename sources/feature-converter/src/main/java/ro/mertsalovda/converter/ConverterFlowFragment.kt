package ro.mertsalovda.converter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ro.mertsalovda.converter.di.ConverterComponent
import ro.mertsalovda.converter.navigation.IViewRouter
import ru.mertsalovda.core_api.providers.AppProvider
import ru.mertsalovda.core_api.providers.AppWithFacade
import javax.inject.Inject

class ConverterFlowFragment : Fragment() {

    @Inject
    lateinit var viewRouter: IViewRouter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fr_flow_converter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ConverterComponent.create((requireActivity().application as AppWithFacade).getFacade() as AppProvider)
            .inject(this)

        viewRouter.showConverter(CONTAINER_ID, childFragmentManager)
    }

    companion object {

        val CONTAINER_ID = R.id.converterContainerId

        @JvmStatic
        fun newInstance() = ConverterFlowFragment()
    }
}