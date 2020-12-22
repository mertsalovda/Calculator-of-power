package ro.mertsalovda.converter.ui.currency

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import ro.mertsalovda.converter.R
import ro.mertsalovda.converter.databinding.FrCurrencyListBinding
import ru.mertsalovda.core_api.dto.Currency
import ru.mertsalovda.core_api.interfaces.IScreenWithTabLayout

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

        (activity as IScreenWithTabLayout).hideTabLayout()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = CurrencyAdapter { /* обработка нажатия на элемент списка */ }
        binding.recycler.adapter = adapter

        setOnBackPressedHolder(view)

        adapter.setData(listOf(Currency(), Currency(), Currency(), Currency()))
    }

    private fun setOnBackPressedHolder(view: View) {
        view.isFocusableInTouchMode = true;
        view.requestFocus()
        view.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                val count = parentFragmentManager.backStackEntryCount
                if (count > 0) {
                    parentFragmentManager.popBackStack()
                }
            }
            true
        }
    }

    override fun onDestroy() {
        (activity as IScreenWithTabLayout).showTabLayout()
        super.onDestroy()
    }

}