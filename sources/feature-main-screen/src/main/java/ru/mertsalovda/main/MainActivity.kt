package ru.mertsalovda.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayout
import ru.mertsalovda.core_api.mediator.AppWithFacade
import ru.mertsalovda.core_api.mediator.BasicCalculatorMediator
import ru.mertsalovda.core_api.mediator.ConverterMediator
import ru.mertsalovda.core_api.mediator.ScientificCalculatorMediator
import ru.mertsalovda.main.databinding.AcMainBinding
import ru.mertsalovda.main.di.MainComponent
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: AcMainBinding

    @Inject
    lateinit var basicCalculatorMediator: BasicCalculatorMediator

    @Inject
    lateinit var scientificCalculatorMediator: ScientificCalculatorMediator

    @Inject
    lateinit var converterMediator: ConverterMediator

    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = AcMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
        setContentView(R.layout.ac_main)

        MainComponent.create((application as AppWithFacade).getFacade()).inject(this)

        tabLayout = findViewById(R.id.tabLayout)

        converterMediator.openConverterScreen(R.id.container, supportFragmentManager)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    when (it.position) {
                        0 -> converterMediator.openConverterScreen(R.id.container, supportFragmentManager)
                        1 -> basicCalculatorMediator.openBasicScreen(R.id.container, supportFragmentManager)
                        2 -> scientificCalculatorMediator.openScientificScreen(R.id.container, supportFragmentManager)
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    fun hideTabLayout() {
        tabLayout.visibility = View.GONE
    }

    fun showTabLayout() {
        tabLayout.visibility = View.VISIBLE
    }
}