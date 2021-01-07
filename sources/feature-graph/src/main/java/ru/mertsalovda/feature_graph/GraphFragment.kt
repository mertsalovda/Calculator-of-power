package ru.mertsalovda.feature_graph

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mertsalovda.feature_graph.databinding.FrGraficBinding

class GraphFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = GraphFragment()
    }

    private lateinit var binding: FrGraficBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fr_grafic, container, false)
        binding = FrGraficBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setListeners()
    }

    private fun setListeners() {
        binding.zoomIn.setOnClickListener {
            val scale = binding.graphView.scale
            binding.graphView.setScale(scale + 0.1f)
        }

        binding.zoomOut.setOnClickListener {
            val scale = binding.graphView.scale
            binding.graphView.setScale(scale - 0.1f)
        }
    }
}