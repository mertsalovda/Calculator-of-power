package ru.mertsalovda.core_api.interfaces

/**
 * Реализовать у Activity/Fragment, который содержит TabLayout,
 * чтобы управлять видимостью TabLayout
 */
interface IScreenWithTabLayout {

    fun hideTabLayout()

    fun showTabLayout()
}