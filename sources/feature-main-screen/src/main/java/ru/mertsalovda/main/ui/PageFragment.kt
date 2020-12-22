package ru.mertsalovda.main.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mertsalovda.core_api.mediator.AppWithFacade
import ru.mertsalovda.core_api.mediator.BasicCalculatorMediator
import ru.mertsalovda.core_api.mediator.ConverterMediator
import ru.mertsalovda.core_api.mediator.ScientificCalculatorMediator
import ru.mertsalovda.main.R
import ru.mertsalovda.main.di.MainComponent
import javax.inject.Inject

class PageFragment : Fragment() {

    /** Страница которую надо отобразить */
    private lateinit var page: Page

    @Inject
    lateinit var basicCalculatorMediator: BasicCalculatorMediator

    @Inject
    lateinit var scientificCalculatorMediator: ScientificCalculatorMediator

    @Inject
    lateinit var converterMediator: ConverterMediator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fr_page, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        MainComponent.create((requireActivity().application as AppWithFacade).getFacade()).inject(this)

        // Загрузить модуль
        when (page) {
            Page.CONVERTER -> converterMediator.openConverterScreen(R.id.pageContainer, childFragmentManager)
            Page.BASIC_CALCULATOR -> basicCalculatorMediator.openBasicScreen(R.id.pageContainer, childFragmentManager)
            Page.SCIENTIFIC_CALCULATOR -> scientificCalculatorMediator.openScientificScreen(R.id.pageContainer, childFragmentManager)
        }
    }

    private fun setPage(page: Page) {
        this.page = page
    }

    companion object {

        @JvmStatic
        fun newInstance(page: Page): PageFragment {
            val fragment = PageFragment()
            fragment.setPage(page)
            return fragment
        }

    }
}