package ru.mertsalovda.main.ui

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {

    /** @return количество страниц определённых в [Page] */
    override fun getItemCount(): Int = Page.values().size

    override fun createFragment(position: Int): PageFragment {
        return PageFragment.newInstance(Page.values()[position])
    }
}