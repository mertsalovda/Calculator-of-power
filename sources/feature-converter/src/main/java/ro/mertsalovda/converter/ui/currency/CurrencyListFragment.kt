package ro.mertsalovda.converter.ui.currency

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ro.mertsalovda.converter.R
import ro.mertsalovda.converter.databinding.FrCurrencyListBinding
import ro.mertsalovda.converter.di.ConverterComponent
import ro.mertsalovda.converter.viewmodel.factory.ConverterViewModelFactory
import ru.mertsalovda.core_api.database.CalculatorDao
import ru.mertsalovda.core_api.interfaces.IScreenWithTabLayout
import ru.mertsalovda.core_api.network.CountriesApi
import ru.mertsalovda.core_api.providers.AppProvider
import javax.inject.Inject

class CurrencyListFragment : Fragment() {

    @Inject
    lateinit var countriesApi: CountriesApi

    @Inject
    lateinit var calculatorDao: CalculatorDao

    private var onCurrencySelected: ((CurrencyItem) -> Unit)? = null

    private lateinit var binding: FrCurrencyListBinding

    private lateinit var viewModel: CurrencyListViewModel

    private lateinit var adapter: CurrencyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ConverterComponent.create(object : AppProvider {
            override fun provideContext(): Context = requireActivity().applicationContext
        }).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fr_currency_list, container, false)
        binding = FrCurrencyListBinding.bind(view)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as IScreenWithTabLayout).hideTabLayout()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(
            this,
            ConverterViewModelFactory.getCurrencyViewModelFactory(countriesApi, calculatorDao)
        ).get(CurrencyListViewModel::class.java)

        adapter = CurrencyAdapter { currencyItem ->
            onCurrencySelected?.let {
                it.invoke(currencyItem)
                val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(this.requireView().findFocus().windowToken, 0)
                parentFragmentManager.popBackStack()
            }
        }
        binding.recycler.adapter = adapter

        setHasOptionsMenu(true)
        setOnBackPressedHolder(view)
        setToolbar()
        setObservers()
        setListeners()
        viewModel.loadCurrencyList()
    }

    private fun setToolbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        val toolbar = (requireActivity() as AppCompatActivity).supportActionBar
        toolbar?.let{
            it.setDisplayHomeAsUpEnabled(true)
            it.title = requireContext().getString(R.string.choose_currency)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home){
            parentFragmentManager.popBackStack()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
    private fun setListeners() {
        binding.queryEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(editable: Editable?) {
                editable?.let {
                    viewModel.setSearchQuery(it.toString())
                }
            }

        })

        binding.refresher.setOnRefreshListener {
            viewModel.loadCurrencyList()
        }
    }

    private fun setObservers() {
        viewModel.getCountriesByQuery().observe(viewLifecycleOwner) {
            adapter.setData(it)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.refresher.isRefreshing = it
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            it?.let {
                Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
            }
        }
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

    companion object {
        fun newInstance(onCurrencySelected: ((CurrencyItem) -> Unit)?): CurrencyListFragment {
            val fragment = CurrencyListFragment()
            fragment.setCurrencySelectedListener(onCurrencySelected)
            return fragment
        }
    }

    fun setCurrencySelectedListener(onCurrencySelected: ((CurrencyItem) -> Unit)?) {
        this.onCurrencySelected = onCurrencySelected
    }

}