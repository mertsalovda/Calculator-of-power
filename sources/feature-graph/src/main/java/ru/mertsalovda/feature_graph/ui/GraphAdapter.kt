package ru.mertsalovda.feature_graph.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.mertsalovda.feature_graph.R
import ru.mertsalovda.feature_graph.databinding.ItemAddGraphBinding
import ru.mertsalovda.feature_graph.databinding.ItemGraphBinding

/**
 * Адаптер для списка графиков.
 *
 * @property clickListener - слушатель нажатия на элемент списка.
 * @property addListener - слушатель нажатия на элемент [AddViewHolder].
 * @property clickDelete - слушатель нажатия на кнопку удалить элемент из списка.
 * @property clickVisibility - слушатель нажатия на кнопку управления видимостью графика.
 */
class GraphAdapter(
    private val clickListener: ((GraphItem) -> Unit)? = null,
    private val addListener: (() -> Unit)? = null,
    private val clickDelete: ((GraphItem) -> Unit)? = null,
    private val clickVisibility: ((GraphItem) -> Unit)? = null,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private companion object {
        const val ADD_ITEM = 0
        const val GRAPH_ITEM = 1
    }

    private var items = mutableListOf<ListItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ADD_ITEM -> {
                val view = layoutInflater.inflate(R.layout.item_add_graph, parent, false)
                AddViewHolder(view)
            }
            GRAPH_ITEM -> {
                val view = layoutInflater.inflate(R.layout.item_graph, parent, false)
                GraphViewHolder(view)
            }
            else -> throw IllegalStateException("Неизвестный тип $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is GraphViewHolder -> holder.bind(
                items[position],
                clickListener,
                clickDelete,
                clickVisibility
            )
            is AddViewHolder -> holder.bind(addListener)
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int =
        when (items[position]) {
            is GraphItem -> GRAPH_ITEM
            else -> ADD_ITEM
        }

    /**
     * Установить данные в список
     * @param data - данные.
     * @param toClear - очистить старые данные перед добавлением новых.
     * */
    fun setData(data: MutableList<ListItem>, toClear: Boolean = false) {
        if (items.isNotEmpty()) {
            items.removeLast()
        }
        if (toClear) {
            items.clear()
        }
        items.addAll(data)
        items.add(ListItem())
        notifyDataSetChanged()
    }

    /** GraphViewHolder отвечает за отображение информации о графике в списке */
    class GraphViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding: ItemGraphBinding = ItemGraphBinding.bind(itemView)

        /**
         * Привязка данных к View
         *
         * @param item - элемент списка [GraphItem] содержащий описание графика.
         * @property clickListener - слушатель нажатия на элемент списка.
         * @property clickDelete - слушатель нажатия на кнопку удалить элемент из списка.
         * @property clickVisibility - слушатель нажатия на кнопку управления видимостью графика.
         */
        fun bind(
            item: ListItem,
            clickListener: ((GraphItem) -> Unit)?,
            clickDelete: ((GraphItem) -> Unit)?,
            clickVisibility: ((GraphItem) -> Unit)?,
        ) {
            if (item is GraphItem) {
                setVisibilityIndicator(item)
                binding.colorMarker.setBackgroundColor(item.markerColor)
                binding.expressionText.text = item.expression

                binding.deleteBtn.setOnClickListener { clickDelete?.let { it.invoke(item) } }

                binding.visibilityBtn.setOnClickListener {
                    item.isVisible = !item.isVisible
                    setVisibilityIndicator(item)
                    clickVisibility?.let { it.invoke(item) }
                }

                binding.root.setOnClickListener { clickListener?.let { it.invoke(item) } }
            }
        }

        /** Установить изображение для кнопки управления видимостью графика */
        private fun setVisibilityIndicator(graphItem: GraphItem) {
            val context = itemView.context
            val image = if (graphItem.isVisible) {
                ContextCompat.getDrawable(context, R.drawable.ic_baseline_visibility_24)
            } else {
                ContextCompat.getDrawable(context, R.drawable.ic_baseline_visibility_off_24)
            }
            binding.visibilityBtn.setImageDrawable(image)
        }
    }

    /** AddViewHolder отвечает за отображение кнопки добавить в конце списка */
    class AddViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding: ItemAddGraphBinding = ItemAddGraphBinding.bind(itemView)

        fun bind(addListener: (() -> Unit)?) {
            binding.addBtn.setOnClickListener { addListener?.let { it.invoke() } }
        }
    }
}