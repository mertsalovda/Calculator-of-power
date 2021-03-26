package ru.mertsalovda.feature_graph.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.mertsalovda.feature_graph.R
import ru.mertsalovda.feature_graph.databinding.FrGraphBinding

class GraphFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = GraphFragment()
    }

    private var _binding: FrGraphBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: GraphViewModel
    private lateinit var adapter: GraphAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fr_graph, container, false)
        _binding = FrGraphBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = GraphAdapter(
            addListener = {
                viewModel.createNewGraph(childFragmentManager)
            },
            clickDelete = {
                viewModel.deleteGraph(it)
            },
            clickListener = {
                viewModel.drawGraph(it)
            },
            clickVisibility = {
                viewModel.drawAllGraph()
                viewModel.drawGraph(it)
            }
        )
        binding.graphList.adapter = adapter

        initViewModel()
        initObservers()
        initListeners()
    }

    override fun onResume() {
        super.onResume()
        viewModel.drawAllGraph()
    }

    private fun initObservers() {
        viewModel.graphItems.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                adapter.setData(it, true)
            }
        })

        viewModel.graphs.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.graphView.addGraphList(it, true)
            }
        })

        viewModel.graph.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.graphView.addGraph(it)
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