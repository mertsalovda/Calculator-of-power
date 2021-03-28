package ro.mertsalovda.converter.ui.values

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
import dagger.Lazy
import ro.mertsalovda.converter.R
import ro.mertsalovda.converter.databinding.FrValueListBinding
import ro.mertsalovda.converter.di.ConverterComponent
import ro.mertsalovda.converter.ui.converter.Mode
import ro.mertsalovda.converter.viewmodel.factory.ConverterViewModelFactory
import ru.mertsalovda.core_api.database.entity.Value
import ru.mertsalovda.core_api.interfaces.IScreenWithTabLayout
import ru.mertsalovda.core_api.providers.AppProvider
import ru.mertsalovda.core_api.providers.AppWithFacade
import javax.inject.Inject

class ValueListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: Lazy<ConverterViewModelFactory>

    private var onCurrencySelected: ((Value) -> Unit)? = null

    private var mode: Mode? = null

    private var codeFilter: String? = null

    private lateinit var binding: FrValueListBinding

    private lateinit var viewModel: ValueListViewModel

    private lateinit var adapter: ValueAdapter

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
        val view = inflater.inflate(R.layout.fr_value_list, container, false)
        binding = FrValueListBinding.bind(view)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as IScreenWithTabLayout).hideTabLayout()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel =
            ViewModelProvider(this, viewModelFactory.get()).get(ValueListViewModel::class.java)

        adapter = ValueAdapter { currencyItem ->
            onCurrencySelected?.let {
                it.invoke(currencyItem)
                val imm =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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
        if (savedInstanceState == null) {
            viewModel.setFilter(codeFilter)
            viewModel.setMode(mode)
        }
        viewModel.loadValueList()
    }
    private fun setToolbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        val toolbar = (requireActivity() as AppCompatActivity).supportActionBar
        toolbar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = requireContext().getString(R.string.choose_currency)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
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
                editable?.let { text ->
                    viewModel.setSearchQuery(text.toString())
                }
            }
        })

        binding.refresher.setOnRefreshListener {
            viewModel.loadValueList()
        }
    }

    private fun setObservers() {
        viewModel.getCountriesByQuery().observe(viewLifecycleOwner) { valueList ->
            adapter.setData(valueList)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.refresher.isRefreshing = isLoading
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let { error ->
                Snackbar.make(requireView(), error, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun setOnBackPressedHolder(view: View) {
        view.isFocusableInTouchMode = true
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
        fun newInstance(
            mode: Mode,
            codeFilter: String?,
            onCurrencySelected: ((Value) -> Unit)?
        ): ValueListFragment {
            val fragment = ValueListFragment()
            fragment.setCurrencySelectedListener(mode, codeFilter, onCurrencySelected)
            return fragment
        }
    }

    fun setCurrencySelectedListener(
        mode: Mode,
        codeFilter: String?,
        onCurrencySelected: ((Value) -> Unit)?
    ) {
        this.mode = mode
        this.codeFilter = codeFilter
        this.onCurrencySelected = onCurrencySelected
    }

}