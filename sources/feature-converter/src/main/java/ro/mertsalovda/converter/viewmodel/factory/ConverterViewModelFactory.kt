package ro.mertsalovda.converter.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ro.mertsalovda.converter.navigation.ViewRouter
import ro.mertsalovda.converter.ui.converter.ConverterViewModel
import ro.mertsalovda.converter.ui.currency.CurrencyListViewModel
import ru.mertsalovda.core_api.database.CalculatorDao
import ru.mertsalovda.core_api.network.CountriesApi
import ru.mertsalovda.core_api.network.ExchangeRatesApi
import java.lang.Exception

class ConverterViewModelFactory private constructor() : ViewModelProvider.Factory {

    private var exchangeRatesApi: ExchangeRatesApi? = null
    private var viewRouter: ViewRouter? = null
    private var calculatorDao: CalculatorDao? = null
    private var countriesApi: CountriesApi? = null


    companion object {
        private var factory: ConverterViewModelFactory? = null

        @JvmStatic
        fun getCurrencyViewModelFactory(
            countriesApi: CountriesApi,
            calculatorDao: CalculatorDao
        ): ConverterViewModelFactory {
            if (factory == null) {
                factory = ConverterViewModelFactory()
            }
            factory!!.setDependencies(countriesApi = countriesApi, calculatorDao = calculatorDao)
            return factory!!
        }

        @JvmStatic
        fun getConverterViewModelFactory(
            exchangeRatesApi: ExchangeRatesApi,
            viewRouter: ViewRouter,
            calculatorDao: CalculatorDao
        ): ConverterViewModelFactory {
            if (factory == null) {
                factory = ConverterViewModelFactory()
            }
            factory!!.setDependencies(exchangeRatesApi, viewRouter, calculatorDao)
            return factory!!
        }
    }

    private fun setDependencies(
        exchangeRatesApi: ExchangeRatesApi? = null,
        viewRouter: ViewRouter? = null,
        calculatorDao: CalculatorDao? = null,
        countriesApi: CountriesApi? = null,
    ) {
        if (this.exchangeRatesApi == null)
            this.exchangeRatesApi = exchangeRatesApi

        if (this.viewRouter == null)
            this.viewRouter = viewRouter

        if (this.calculatorDao == null)
            this.calculatorDao = calculatorDao

        if (this.countriesApi == null)
            this.countriesApi = countriesApi
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            ConverterViewModel::class.java -> ConverterViewModel(
                exchangeRatesApi!!,
                viewRouter!!,
                calculatorDao!!
            ) as T
            CurrencyListViewModel::class.java -> CurrencyListViewModel(
                countriesApi!!,
                calculatorDao!!
            ) as T
            else -> throw Exception("$modelClass невозможно создать.")
        }
    }
}