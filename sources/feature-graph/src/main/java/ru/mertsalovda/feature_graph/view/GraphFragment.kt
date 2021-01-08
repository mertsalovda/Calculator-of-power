package ru.mertsalovda.feature_graph.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.mertsalovda.feature_graph.R
import ru.mertsalovda.feature_graph.databinding.FrGraficBinding

class GraphFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = GraphFragment()
    }

    private var _binding: FrGraficBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: GraphViewModel
    private lateinit var adapter: GraphAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fr_grafic, container, false)
        _binding = FrGraficBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = GraphAdapter(
            addListener = {
                viewModel.createNewGraph(childFragmentManager)
            },
            clickDelete = {
                viewModel.deleteGraph(it)
            }
        )
        binding.graphList.adapter = adapter

        initViewModel()
        initObservers()
        initListeners()
    }

    private fun initObservers() {
        viewModel.graph.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                adapter.setData(it, true)
            }
        })
    }

    private fun initViewModel() {
        viewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
                .create(GraphViewModel::class.java)
    }

    private fun initListeners() {
        binding.zoomIn.setOnClickListener {
            val scale = binding.graphView.scale
            binding.graphView.setScale(scale + 0.1f)
        }

        binding.zoomOut.setOnClickListener {
            val scale = binding.graphView.scale
            binding.graphView.setScale(scale - 0.1f)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}