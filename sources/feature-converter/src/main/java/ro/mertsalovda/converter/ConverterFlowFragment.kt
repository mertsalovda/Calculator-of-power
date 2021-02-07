package ro.mertsalovda.converter

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ro.mertsalovda.converter.di.ConverterComponent
import ro.mertsalovda.converter.navigation.ViewRouter
import ru.mertsalovda.core_api.providers.AppProvider
import javax.inject.Inject

class ConverterFlowFragment : Fragment() {

    @Inject
    lateinit var viewRouter: ViewRouter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fr_flow_converter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ConverterComponent.create(
            object : AppProvider {
                override fun provideContext(): Context = requireActivity().applicationContext
            }
        ).inject(this)

        viewRouter.showConverter(CONTAINER_ID, childFragmentManager)
    }

    companion object {

        val CONTAINER_ID = R.id.converterContainerId

        @JvmStatic
        fun newInstance() = ConverterFlowFragment()
    }
}