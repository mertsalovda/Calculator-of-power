package ro.mertsalovda.converter.di

import dagger.Binds
import dagger.Module
import dagger.Reusable
import ro.mertsalovda.converter.navigation.ViewRouter
import ro.mertsalovda.converter.navigation.ViewRouterImpl

@Module
interface ConverterModule {

    @Binds
    @Reusable
    fun bindsMainMediator(viewRouter: ViewRouterImpl): ViewRouter
}