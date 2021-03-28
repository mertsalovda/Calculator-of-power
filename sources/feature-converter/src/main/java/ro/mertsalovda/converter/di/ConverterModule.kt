package ro.mertsalovda.converter.di

import dagger.Binds
import dagger.Module
import dagger.Reusable
import ro.mertsalovda.converter.navigation.IViewRouter
import ro.mertsalovda.converter.navigation.ViewRouterImpl
import ro.mertsalovda.converter.repository.CurrencyConverterRepository
import ro.mertsalovda.converter.repository.ICurrencyConverterRepository
import ro.mertsalovda.converter.repository.IPhysicalValueRepository
import ro.mertsalovda.converter.repository.PhysicalValueRepository

@Module
interface ConverterModule {

    @Binds
    @Reusable
    fun bindsMainMediator(viewRouter: ViewRouterImpl): IViewRouter

    @Binds
    @Reusable
    fun bindsPhysicalValueRepository(physicalValueRepository: PhysicalValueRepository): IPhysicalValueRepository

    @Binds
    @Reusable
    fun bindsCurrencyConverterRepository(currencyConverterRepository: CurrencyConverterRepository): ICurrencyConverterRepository
}