package ru.mertsalovda.calculatorofpower

import android.app.Application
import ru.mertsalovda.core_api.mediator.AppWithFacade
import ru.mertsalovda.core_api.providers.ProvidersFacade

class App : Application(), AppWithFacade {

    companion object {

        private var facadeComponent: FacadeComponent? = null
    }

    override fun getFacade(): ProvidersFacade {
        return facadeComponent ?: FacadeComponent.init(this).also {
            facadeComponent = it
        }
    }

    override fun onCreate() {
        super.onCreate()
        (getFacade() as FacadeComponent).inject(this)
    }
}