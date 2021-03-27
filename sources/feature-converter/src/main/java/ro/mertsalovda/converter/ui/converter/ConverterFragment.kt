package ro.mertsalovda.converter.ui.converter

import android.annotation.SuppressLint
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
import dagger.Lazy
import ro.mertsalovda.converter.ConverterFlowFragment
import ro.mertsalovda.converter.R
import ro.mertsalovda.converter.databinding.FrConverterBinding
import ro.mertsalovda.converter.databinding.KeypadNumericBinding
import ro.mertsalovda.converter.di.ConverterComponent
import ro.mertsalovda.converter.utils.GlideApp
import ro.mertsalovda.converter.utils.SvgSoftwareLayerSetter
import ro.mertsalovda.converter.viewmodel.factory.ConverterViewModelFactory
import ru.mertsalovda.core_api.database.entity.Value
import ru.mertsalovda.core_api.providers.AppProvider
import ru.mertsalovda.core_api.providers.AppWithFacade
import javax.inject.Inject

class ConverterFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: Lazy<ConverterViewModelFactory>

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
        binding = FrConverterBinding.inflate(inflater)
        keypad = KeypadNumericBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(this, viewModelFactory.get()).get(ConverterViewModel::class.java)

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

        for (key in keypadMap.keys) {
            keypadMap[key]?.setOnClickListener { view: View ->
                viewModel.clickKeypad(view.tag.toString())
            }
        }

        binding.currencyChip.isChecked = true

        binding.groupConverterUnit.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.currencyChip -> viewModel.setMode(Mode.CURRENCY)
                R.id.lengthChip -> viewModel.setMode(Mode.LENGTH)
                R.id.weightChip -> viewModel.setMode(Mode.WEIGHT)
                R.id.speedChip -> viewModel.setMode(Mode.SPEED)
                R.id.areaChip -> viewModel.setMode(Mode.AREA)
            }
        }
    }

    private fun setObservers() {
        viewModel.unitPreview1.observe(viewLifecycleOwner) { valueItem ->
            valueItem?.let { updatePreview(ConverterValue.CONVERTED_VALUE, it) }
                ?: clearPreview(ConverterValue.CONVERTED_VALUE)
        }
        viewModel.unitPreview2.observe(viewLifecycleOwner) { valueItem ->
            valueItem?.let { updatePreview(ConverterValue.RESULT_VALUE, it) }
                ?: clearPreview(ConverterValue.RESULT_VALUE)
        }
        viewModel.unit1.observe(viewLifecycleOwner) { value ->
            (binding.etUnit1 as TextView).text = value
        }
        viewModel.unit2.observe(viewLifecycleOwner) { value ->
            (binding.etUnit2 as TextView).text = value
        }
    }

    /**
     * Очистить отображение конвертируемой величины
     * @param converterValue    фокус на ConverterValue
     */
    @SuppressLint("SetTextI18n")
    private fun clearPreview(converterValue: ConverterValue) {
        when (converterValue) {
            ConverterValue.CONVERTED_VALUE -> {
                setImage(binding.iconUnit1, null)
                binding.titleUnit1.text = "NONE"
                viewModel.loadExchangeRateForBaseCurrency()
            }
            ConverterValue.RESULT_VALUE -> {
                setImage(binding.iconUnit2, null)
                binding.titleUnit2.text = "NONE"
            }
        }
    }

    /**
     * Обновляет отображение конвертируемой величины
     * @param converterValue    фокус на ConverterValue
     * @param valueItem         данные конвертируемой величине
     */
    private fun updatePreview(converterValue: ConverterValue, valueItem: Value) {
        when (converterValue) {
            ConverterValue.CONVERTED_VALUE -> {
                setImage(binding.iconUnit1, valueItem)
                binding.titleUnit1.text = valueItem.code
                if (valueItem is Value.Currency) {
                    viewModel.loadExchangeRateForBaseCurrency()
                }
            }
            ConverterValue.RESULT_VALUE -> {
                setImage(binding.iconUnit2, valueItem)
                binding.titleUnit2.text = valueItem.code
            }
        }
        if (valueItem !is Value.Currency) {
            viewModel.loadConversionFactor()
        }
    }

    /** Получить [ConverterValue] к которому относится view */
    private fun getSelectedValue(view: View): ConverterValue? = when (view.id) {
        R.id.etUnit1, R.id.unitContainer1, R.id.currencySelectBtn1 -> ConverterValue.CONVERTED_VALUE
        R.id.etUnit2, R.id.unitContainer2, R.id.currencySelectBtn2 -> ConverterValue.RESULT_VALUE
        else -> null
    }

    /**
     * Установить изображение в ImageView
     * @param item ссылка на изображение
     */
    private fun setImage(imageView: ImageView, item: Value?) {
        if (item is Value.Currency) {
            // Загружаю изображение .svg
            GlideApp.with(imageView.context.applicationContext)
                .`as`(PictureDrawable::class.java)
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_adaptation)
                .load(item.image)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .listener(SvgSoftwareLayerSetter())
                .into(imageView)
        } else {
            GlideApp.with(imageView.context.applicationContext)
                .load(item?.image?.toIntOrNull())
                .error(R.drawable.ic_adaptation)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(imageView)
        }
    }
}