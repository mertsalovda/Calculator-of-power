package ru.mertsalovda.feature_graph.view.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.mertsalovda.feature_graph.R
import ru.mertsalovda.feature_graph.databinding.FrNewGraphBinding
import ru.mertsalovda.feature_graph.databinding.KeypadGraphBinding
import ru.mertsalovda.feature_graph.view.graph.Graph

/** Фрагмент для создания нового графика */
class NewGraphFragment : DialogFragment() {

    private var _binding: FrNewGraphBinding? = null
    private var _keypad: KeypadGraphBinding? = null

    private val binding get() = _binding!!
    private val keypad get() = _keypad!!

    private lateinit var callback: (Graph)->Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fr_new_graph, container, false)
        _binding = FrNewGraphBinding.bind(view)
        _keypad = KeypadGraphBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        keypad.btnOk.setOnClickListener {
            val graph = Graph(
                expression = binding.etExpression.text.toString(),
                points = listOf(),
                color = Color.RED
            )
            callback.invoke(graph)
            dismiss()
        }
/*        requireDialog().setOnShowListener {
            val dialog = it as BottomSheetDialog
            val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet)
            bottomSheet?.let { sheet ->
                dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
                dialog.behavior.peekHeight = sheet.height
                sheet.parent.parent.requestLayout()
            }
        }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _keypad = null
    }

    private fun setCallback(callback: (Graph) -> Unit) {
        this.callback = callback
    }

    companion object {
        @JvmStatic
        fun newInstance(callback: (Graph) -> Unit): NewGraphFragment{
            val fragment = NewGraphFragment()
            fragment.setCallback(callback)
            return fragment
        }
    }
}