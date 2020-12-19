package ru.mertsalovda.scientific

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mertsalovda.scientific.databinding.FrScientificCalculatorBinding

class ScientificCalculatorFragment : Fragment() {

    private lateinit var binding: FrScientificCalculatorBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fr_scientific_calculator, container, false)
        binding = FrScientificCalculatorBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.etExpression.inputType = InputType.TYPE_NULL
    }

    companion object {
        @JvmStatic
        fun newInstance() = ScientificCalculatorFragment()
    }
}