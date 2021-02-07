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

class ConverterViewModelFactory private constructor(): ViewModelProvider.Factory {

    private lateinit var exchangeRatesApi: ExchangeRatesApi
    private lateinit var viewRouter: ViewRouter
    private lateinit var calculatorDao: CalculatorDao
    private lateinit var countriesApi: CountriesApi

    companion object {
        @JvmStatic
        fun getCurrencyViewModelFactory(countriesApi: CountriesApi): ConverterViewModelFactory {
            val factory = ConverterViewModelFactory()
            factory.setDependencies(countriesApi)
            return factory
        }

        @JvmStatic
        fun getConverterViewModelFactory(
            exchangeRatesApi: ExchangeRatesApi,
            viewRouter: ViewRouter,
            calculatorDao: CalculatorDao
        ): ConverterViewModelFactory {
            val factory = ConverterViewModelFactory()
            factory.setDependencies(exchangeRatesApi, viewRouter, calculatorDao)
            return factory
        }
    }

    private fun setDependencies(
        exchangeRatesApi: ExchangeRatesApi,
        viewRouter: ViewRouter,
        calculatorDao: CalculatorDao
    ) {
        this.exchangeRatesApi = exchangeRatesApi
        this.viewRouter = viewRouter
        this.calculatorDao = calculatorDao
    }

    private fun setDependencies(countriesApi: CountriesApi) {
        this.countriesApi = countriesApi
    }


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            ConverterViewModel::class.java -> ConverterViewModel(
                exchangeRatesApi,
                viewRouter,
                calculatorDao
            ) as T
            CurrencyListViewModel::class.java -> CurrencyListViewModel(countriesApi) as T
            else -> throw Exception("$modelClass невозможно создать. Нужен ConverterViewModel::class")
        }
    }
}