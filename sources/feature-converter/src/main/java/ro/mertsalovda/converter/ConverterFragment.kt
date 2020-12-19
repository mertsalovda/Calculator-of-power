package ro.mertsalovda.converter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ro.mertsalovda.converter.databinding.FrConverterBinding

class ConverterFragment : Fragment() {

    lateinit var binding: FrConverterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fr_converter, container, false)
        binding = FrConverterBinding.bind(view)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = ConverterFragment()
    }
}