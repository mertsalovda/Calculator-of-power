package ro.mertsalovda.converter.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ro.mertsalovda.converter.ui.converter.ConverterViewModel
import dagger.Lazy
import ro.mertsalovda.converter.ui.values.ValueListViewModel
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Фабрика для сборки ViewModel
 *
 * @property viewRouter                     роутер для навигации в модуле
 * @property currencyRepository             репозиторий для получения списка валют по странам
 * @property currencyConverterRepository    репозиторий для получения обменного курса валют
 * @property physicalValueRepository        репозиторий физических величин
 */
@Singleton
class ConverterViewModelFactory @Inject constructor(
    private val converterViewModel: Lazy<ConverterViewModel>,
    private val valueListViewModel: Lazy<ValueListViewModel>,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            ConverterViewModel::class.java -> converterViewModel.get() as T
            ValueListViewModel::class.java -> valueListViewModel.get() as T
            else -> throw Exception("$modelClass невозможно создать.")
        }
    }
}