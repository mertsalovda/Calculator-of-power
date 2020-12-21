package ro.mertsalovda.converter.ui.currency

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import ro.mertsalovda.converter.R
import ro.mertsalovda.converter.databinding.FrCurrencyListBinding
import ru.mertsalovda.core_api.dto.Currency

class CurrencyListFragment : Fragment() {

    companion object {
        fun newInstance() = CurrencyListFragment()
    }

    private lateinit var binding: FrCurrencyListBinding

    private lateinit var viewModel: CurrencyListViewModel

    private lateinit var adapter: CurrencyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fr_currency_list, container, false)
        binding = FrCurrencyListBinding.bind(view)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CurrencyListViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = CurrencyAdapter {  }
        binding.recycler.adapter = adapter

        adapter.setData(listOf(Currency(), Currency(), Currency(), Currency()))

    }

}