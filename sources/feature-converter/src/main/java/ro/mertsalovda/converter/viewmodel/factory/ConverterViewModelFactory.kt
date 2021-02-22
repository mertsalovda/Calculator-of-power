package ro.mertsalovda.converter.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ro.mertsalovda.converter.navigation.ViewRouter
import ro.mertsalovda.converter.repository.CurrencyConverterRepository
import ro.mertsalovda.converter.repository.CurrencyRepository
import ro.mertsalovda.converter.ui.converter.ConverterViewModel
import ro.mertsalovda.converter.ui.currency.CurrencyListViewModel
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Фабрика для сборки ViewModel
 *
 * @property viewRouter                     роутер для навигации в модуле
 * @property currencyRepository             репозиторий для получения списка валют по странам
 * @property currencyConverterRepository    репозиторий для получения обменного курса валют
 */
@Singleton
class ConverterViewModelFactory @Inject constructor(
    private val viewRouter: ViewRouter,
    private val currencyRepository: CurrencyRepository,
    private val currencyConverterRepository: CurrencyConverterRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            ConverterViewModel::class.java -> ConverterViewModel(
                viewRouter,
                currencyConverterRepository
            ) as T
            CurrencyListViewModel::class.java -> CurrencyListViewModel(
                currencyRepository
            ) as T
            else -> throw Exception("$modelClass невозможно создать.")
        }
    }
}