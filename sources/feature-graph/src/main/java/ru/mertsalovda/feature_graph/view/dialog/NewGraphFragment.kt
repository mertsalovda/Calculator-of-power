package ru.mertsalovda.feature_graph.view.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.DialogFragment
import ru.mertsalovda.feature_graph.R
import ru.mertsalovda.feature_graph.databinding.FrNewGraphBinding
import ru.mertsalovda.feature_graph.view.GraphItem
import ru.mertsalovda.feature_graph.view.graph.Graph
import ru.mertsalovda.feature_graph.view.graph.GraphUtil

/** Фрагмент для создания нового графика */
class NewGraphFragment : DialogFragment() {

    private var _binding: FrNewGraphBinding? = null

    private val binding get() = _binding!!

    private lateinit var callback: (GraphItem)->Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fr_new_graph, container, false)
        _binding = FrNewGraphBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireDialog().setTitle("Введите функцию f(x)")
        binding.etExpression.setOnEditorActionListener { _, actionId, _ ->
            when(actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    val expression = binding.etExpression.text.toString().toLowerCase().replace("\\s+".toRegex(), "")
                    callback.invoke(GraphItem(GraphUtil.getRandomColor(), expression))
                    dismiss()
                    true
                }
                else -> false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setCallback(callback: (GraphItem) -> Unit) {
        this.callback = callback
    }

    companion object {
        @JvmStatic
        fun newInstance(callback: (GraphItem) -> Unit): NewGraphFragment{
            val fragment = NewGraphFragment()
            fragment.setCallback(callback)
            return fragment
        }
    }
}