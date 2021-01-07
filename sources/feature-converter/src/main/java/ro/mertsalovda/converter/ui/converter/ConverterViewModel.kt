package ro.mertsalovda.converter.ui.converter

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ro.mertsalovda.converter.di.ConverterComponent
import ro.mertsalovda.converter.navigation.ViewRouter
import javax.inject.Inject

class ConverterViewModel : ViewModel() {

    @Inject
    lateinit var viewRouter: ViewRouter

    private val _unit1 = MutableLiveData<Long>(0L)
    val unit1: LiveData<Long> = _unit1

    private val _unit2 = MutableLiveData<Long>(0L)
    val unit2: LiveData<Long> = _unit2

    init {
        ConverterComponent.create().inject(this)
    }

    fun showCurrencyList(@IdRes containerId: Int, childFragmentManager: FragmentManager) {
        viewRouter.showCurrencyList(containerId, childFragmentManager)
    }
}