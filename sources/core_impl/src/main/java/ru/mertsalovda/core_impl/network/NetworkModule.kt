package ru.mertsalovda.core_impl.network

import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.mertsalovda.core_api.network.CountriesApi
import ru.mertsalovda.core_api.network.ExchangeRatesApi
import ru.mertsalovda.core_api.network.NewtonApi
import ru.mertsalovda.core_impl.BuildConfig
import javax.inject.Singleton

const val COUNTRIES_BASE_URL = "https://restcountries.eu/rest/v2/"
const val NEWTON_BASE_URL = "https://newton.now.sh/api/v2/"
const val EXCHANGE_RATES_BASE_URL = "https://api.exchangeratesapi.io/"

@Module
class NetworkModule{

    @Provides
    fun provideGson(): Gson = Gson()

    @Provides
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory =
        GsonConverterFactory.create(gson)

    @Provides
    fun provideKotlinCoroutineAdapter() = CoroutineCallAdapterFactory()

    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(interceptor)
        }
        return builder.build()
    }

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Reusable
    fun provideCountriesApi(
        client: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        coroutineCallAdapterFactory: CoroutineCallAdapterFactory
    ): CountriesApi =
        Retrofit.Builder()
            .baseUrl(COUNTRIES_BASE_URL)
            .client(client)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(coroutineCallAdapterFactory)
            .build()
            .create(CountriesApi::class.java)

    @Provides
    @Reusable
    fun provideNewtonApi(
        client: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        coroutineCallAdapterFactory: CoroutineCallAdapterFactory
    ): NewtonApi =
        Retrofit.Builder()
            .baseUrl(NEWTON_BASE_URL)
            .client(client)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(coroutineCallAdapterFactory)
            .build()
            .create(NewtonApi::class.java)

    @Provides
    @Reusable
    fun provideExchangeRatesApi(
        client: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        coroutineCallAdapterFactory: CoroutineCallAdapterFactory
    ): ExchangeRatesApi =
        Retrofit.Builder()
            .baseUrl(EXCHANGE_RATES_BASE_URL)
            .client(client)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(coroutineCallAdapterFactory)
            .build()
            .create(ExchangeRatesApi::class.java)
}