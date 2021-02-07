package ro.mertsalovda.converter.di

import dagger.Component
import ro.mertsalovda.converter.ConverterFlowFragment
import ro.mertsalovda.converter.ui.converter.ConverterFragment
import ro.mertsalovda.converter.ui.converter.ConverterViewModel
import ro.mertsalovda.converter.ui.currency.CurrencyListFragment
import ro.mertsalovda.converter.ui.currency.CurrencyListViewModel
import ru.mertsalovda.core.CoreProvidersFactory
import ru.mertsalovda.core_api.database.DatabaseProvider
import ru.mertsalovda.core_api.providers.AppProvider
import ru.mertsalovda.core_api.providers.NetworkProvider

@Component(
    modules = [ConverterModule::class],
    dependencies = [NetworkProvider::class, DatabaseProvider::class]
)
interface ConverterComponent {

    companion object {

        fun create(appProvider: AppProvider): ConverterComponent {
            return DaggerConverterComponent.builder()
                .databaseProvider(CoreProvidersFactory.createDatabaseBuilder(appProvider))
                .networkProvider(CoreProvidersFactory.createNetworkBuilder()).
                build()
        }
    }

    fun inject(converterFlowFragment: ConverterFlowFragment)
    fun inject(converterFragment: ConverterFragment)
    fun inject(currencyListFragment: CurrencyListFragment)
}