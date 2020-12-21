package ro.mertsalovda.converter.di

import dagger.Component
import ro.mertsalovda.converter.ui.converter.ConverterViewModel
import ru.mertsalovda.core_api.mediator.ProvidersFacade

@Component(
    modules = [ConverterModule::class]
)
interface ConverterComponent {

    companion object {

        fun create(): ConverterComponent {
            return DaggerConverterComponent.builder().build()
        }
    }

    fun inject(converterViewModel: ConverterViewModel)
}