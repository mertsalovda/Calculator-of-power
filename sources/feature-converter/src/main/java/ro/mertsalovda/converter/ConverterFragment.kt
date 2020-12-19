package ro.mertsalovda.converter

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ro.mertsalovda.converter.databinding.FrConverterBinding
import ro.mertsalovda.converter.databinding.KeypadNumericBinding

class ConverterFragment : Fragment() {

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

        keypad.btnBackspace.setOnClickListener{
            var text = binding.etUnit1.text.toString()
            val end = if (text.isNullOrEmpty()) 0 else text.length - 1
            text = text.substring(0, end)
            (binding.etUnit1 as TextView).text = text
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ConverterFragment()
    }
}