package ro.mertsalovda.converter.di

import dagger.Component
import ro.mertsalovda.converter.ConverterFlowFragment
import ro.mertsalovda.converter.ui.converter.ConverterViewModel
import ro.mertsalovda.converter.ui.currency.CurrencyListViewModel
import ru.mertsalovda.core.CoreProvidersFactory
import ru.mertsalovda.core_api.providers.NetworkProvider

@Component(
    modules = [ConverterModule::class],
    dependencies = [NetworkProvider::class]
)
interface ConverterComponent {

    companion object {

        fun create(): ConverterComponent {
            return DaggerConverterComponent.builder().networkProvider(
                CoreProvidersFactory.createNetworkBuilder()
            ).build()
        }
    }

    fun inject(converterViewModel: ConverterViewModel)
    fun inject(converterFlowFragment: ConverterFlowFragment)
    fun inject(currencyListViewModel: CurrencyListViewModel)
}