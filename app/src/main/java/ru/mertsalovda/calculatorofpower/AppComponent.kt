package ru.mertsalovda.calculatorofpower

import android.app.Application
import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.mertsalovda.core_api.providers.AppProvider
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class]
)
interface AppComponent : AppProvider {

    companion object {

        private var appComponent: AppProvider? = null

        /**
         * Создать [AppComponent] приведённый к типу [AppProvider], если он ещё не создан
         *
         * @return возвращает [AppProvider]
         */
        fun create(application: Application): AppProvider {
            return appComponent ?: DaggerAppComponent
                .builder()
                .application(application.applicationContext)
                .build().also {
                    appComponent = it
                }
        }
    }

    /** Билдер для [AppComponent] */
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(context: Context): Builder

        fun build(): AppComponent
    }
}