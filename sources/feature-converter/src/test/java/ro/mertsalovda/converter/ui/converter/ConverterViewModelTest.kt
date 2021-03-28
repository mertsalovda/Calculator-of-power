package ro.mertsalovda.converter.ui.converter

import androidx.fragment.app.FragmentManager
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import ro.mertsalovda.converter.navigation.IViewRouter
import ro.mertsalovda.converter.repository.ICurrencyConverterRepository
import ro.mertsalovda.converter.repository.IPhysicalValueRepository
import ru.mertsalovda.core_api.database.entity.ExchangeRate
import ru.mertsalovda.core_api.network.ExchangeRatesApi
import java.util.*

class ConverterViewModelTest {

    @get:Rule
    val mockRule = MockitoJUnit.rule()

    @Mock
    lateinit var exchangeRatesApi: ExchangeRatesApi

    @Mock
    lateinit var mockCurrencyConverterRepository: ICurrencyConverterRepository

    @Mock
    lateinit var mockPhysicalValueRepository: IPhysicalValueRepository

    @Mock
    lateinit var mockViewRouter: IViewRouter

    @Mock
    lateinit var fragmentManager: FragmentManager

    private lateinit var viewModel: ConverterViewModel

    @ObsoleteCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @ExperimentalCoroutinesApi
    val testDispatcher = TestCoroutineDispatcher()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ConverterViewModel(
            mockViewRouter,
            mockCurrencyConverterRepository,
            mockPhysicalValueRepository,
        )
    }

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun setValueFocused() {
    }

    @Test
    fun showValueList() {
        viewModel.showValueList(anyInt(), fragmentManager)
        verify(mockViewRouter).showValueList(
            anyInt(),
            com.nhaarman.mockitokotlin2.any(),
            fragmentManager,
            com.nhaarman.mockitokotlin2.any(),
            com.nhaarman.mockitokotlin2.any()
        )
    }

    @Test
    fun clickKeypad() {
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadCoefficient(): Unit = runBlockingTest(testDispatcher) {
        val exchangeRate =
            ExchangeRate(base = "USD", rates = hashMapOf("RUM" to 100.0), Date().toString())

        `when`(mockCurrencyConverterRepository.getExchangeRateByBaseCurrency("USD"))
            .thenReturn(exchangeRate)

        viewModel.loadCoefficient()
        val actual =
            mockCurrencyConverterRepository.getExchangeRateByBaseCurrency("USD")
        assertEquals(exchangeRate, actual)
    }

    @Test
    fun setMode() {
    }
}

object MockitoHelper {
    fun <T> anyObject(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> uninitialized(): T = null as T
}