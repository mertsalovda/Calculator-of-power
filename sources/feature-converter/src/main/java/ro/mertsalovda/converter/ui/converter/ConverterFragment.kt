package ro.mertsalovda.converter.ui.converter

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import ro.mertsalovda.converter.R
import ro.mertsalovda.converter.databinding.FrConverterBinding
import ro.mertsalovda.converter.databinding.KeypadNumericBinding

class ConverterFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = ConverterFragment()
    }

    private lateinit var viewModel: ConverterViewModel

    lateinit var binding: FrConverterBinding
    lateinit var keypad: KeypadNumericBinding

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
        binding.etUnit1.inputType = InputType.TYPE_NULL
        binding.etUnit2.inputType = InputType.TYPE_NULL

        binding.currencySelectBtn1.setOnClickListener {
            viewModel.showCurrencyList(0, childFragmentManager) // TODO добавить id контейнера
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ConverterViewModel::class.java)
    }
}