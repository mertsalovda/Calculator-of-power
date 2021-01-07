package ro.mertsalovda.converter.di

import dagger.Component
import ro.mertsalovda.converter.ConverterFlowFragment
import ro.mertsalovda.converter.ui.converter.ConverterViewModel

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
    fun inject(converterFlowFragment: ConverterFlowFragment)
}