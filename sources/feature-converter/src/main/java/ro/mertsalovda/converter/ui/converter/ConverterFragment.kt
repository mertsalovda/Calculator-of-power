package ro.mertsalovda.converter.ui.converter

import android.graphics.drawable.PictureDrawable
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import ro.mertsalovda.converter.ConverterFlowFragment
import ro.mertsalovda.converter.R
import ro.mertsalovda.converter.databinding.FrConverterBinding
import ro.mertsalovda.converter.databinding.KeypadNumericBinding
import ro.mertsalovda.converter.di.ConverterComponent
import ru.mertsalovda.core_api.database.entity.CurrencyItem
import ro.mertsalovda.converter.utils.GlideApp
import ro.mertsalovda.converter.utils.SvgSoftwareLayerSetter
import ro.mertsalovda.converter.viewmodel.factory.ConverterViewModelFactory
import ru.mertsalovda.core_api.providers.AppProvider
import ru.mertsalovda.core_api.providers.AppWithFacade
import javax.inject.Inject

class ConverterFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ConverterViewModelFactory

    companion object {
        @JvmStatic
        fun newInstance() = ConverterFragment()
    }

    private lateinit var viewModel: ConverterViewModel

    lateinit var binding: FrConverterBinding
    lateinit var keypad: KeypadNumericBinding

    private lateinit var keypadMap: Map<String, View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appProvider =
            (requireActivity().application as AppWithFacade).getFacade() as AppProvider
        ConverterComponent.create(appProvider).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fr_converter, container, false)
        binding = FrConverterBinding.bind(view)
        keypad = KeypadNumericBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(this, viewModelFactory).get(ConverterViewModel::class.java)

        disableSoftKeypad()
        initKeypadMap()
        setListeners()
        setObservers()
    }

    private fun disableSoftKeypad() {
        binding.etUnit1.inputType = InputType.TYPE_NULL
        binding.etUnit2.inputType = InputType.TYPE_NULL
    }

    private fun initKeypadMap() {
        keypadMap = hashMapOf(
            "0" to keypad.btnNumber0,
            "1" to keypad.btnNumber1,
            "2" to keypad.btnNumber2,
            "3" to keypad.btnNumber3,
            "4" to keypad.btnNumber4,
            "5" to keypad.btnNumber5,
            "6" to keypad.btnNumber6,
            "7" to keypad.btnNumber7,
            "8" to keypad.btnNumber8,
            "9" to keypad.btnNumber9,
            "." to keypad.btnPoint,
            "Backspace" to keypad.btnBackspace,
        )
    }

    private fun setListeners() {
        val focusChangeListener =
            View.OnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    getSelectedValue(view)?.let { viewModel.setValueFocused(it) }
                }
            }
        binding.etUnit1.onFocusChangeListener = focusChangeListener
        binding.etUnit2.onFocusChangeListener = focusChangeListener

        val onCurrencyClickListener = { view: View ->
            getSelectedValue(view)?.let { viewModel.setValueFocused(it) }
            viewModel.showCurrencyList(ConverterFlowFragment.CONTAINER_ID, parentFragmentManager)
        }
        binding.unitContainer1.setOnClickListener(onCurrencyClickListener)
        binding.unitContainer2.setOnClickListener(onCurrencyClickListener)
        binding.currencySelectBtn1.setOnClickListener(onCurrencyClickListener)
        binding.currencySelectBtn2.setOnClickListener(onCurrencyClickListener)

        val onKeypadClickListener = { view: View ->
            viewModel.clickKeypad(view.tag.toString())
        }
        for (key in keypadMap.keys) {
            keypadMap[key]?.let { it.setOnClickListener(onKeypadClickListener) }
        }
    }

    private fun setObservers() {
        viewModel.unitPreview1.observe(viewLifecycleOwner) { currencyItem ->
            currencyItem?.let { updatePreview(Value.CONVERTED_VALUE, it) }
        }
        viewModel.unitPreview2.observe(viewLifecycleOwner) { currencyItem ->
            currencyItem?.let { updatePreview(Value.RESULT_VALUE, it) }
        }
        viewModel.unit1.observe(viewLifecycleOwner) {
            (binding.etUnit1 as TextView).text = it
        }
        viewModel.unit2.observe(viewLifecycleOwner) {
            (binding.etUnit2 as TextView).text = it
        }
    }

    /**
     * Обновляет отображение конвертируемой величины
     * @param value фокус на Value
     * @param currencyItem данные овалюте
     */
    private fun updatePreview(value: Value, currencyItem: CurrencyItem) {
        when (value) {
            Value.CONVERTED_VALUE -> {
                setImage(binding.iconUnit1, currencyItem.flagUrl)
                binding.titleUnit1.text = currencyItem.currencyCode
                viewModel.loadExchangeRateForBaseCurrency()
            }
            Value.RESULT_VALUE -> {
                setImage(binding.iconUnit2, currencyItem.flagUrl)
                binding.titleUnit2.text = currencyItem.currencyCode
            }
        }
    }

    /** Получить [Value] к которому относится view */
    private fun getSelectedValue(view: View): Value? = when (view.id) {
        R.id.etUnit1, R.id.unitContainer1, R.id.currencySelectBtn1 -> Value.CONVERTED_VALUE
        R.id.etUnit2, R.id.unitContainer2, R.id.currencySelectBtn2 -> Value.RESULT_VALUE
        else -> null
    }

    /**
     * Установить изображение в ImageView
     * @param url ссылка на изображение
     */
    private fun setImage(imageView: ImageView, url: String?) {
        // Загружаю изображение .svg
        GlideApp.with(imageView.context)
            .`as`(PictureDrawable::class.java)
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(R.drawable.ripple_bg_transparent)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .listener(SvgSoftwareLayerSetter())
            .into(imageView)
    }
}