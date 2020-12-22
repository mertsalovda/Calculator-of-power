package ru.mertsalovda.main.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ru.mertsalovda.core_api.interfaces.IScreenWithTabLayout
import ru.mertsalovda.main.R

class MainActivity : AppCompatActivity(), IScreenWithTabLayout {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_main)

        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        viewPager.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = Page.values()[position].title()
        }.attach()
    }

    override fun hideTabLayout() {
        tabLayout.visibility = View.GONE
        viewPager.isUserInputEnabled = false
    }

    override fun showTabLayout() {
        tabLayout.visibility = View.VISIBLE
        viewPager.isUserInputEnabled = true
    }
}