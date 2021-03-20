package ro.mertsalovda.converter.ui.currency

import android.annotation.SuppressLint
import android.graphics.drawable.PictureDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import ro.mertsalovda.converter.R
import ro.mertsalovda.converter.databinding.ItemValueBinding
import ro.mertsalovda.converter.utils.GlideApp
import ro.mertsalovda.converter.utils.SvgSoftwareLayerSetter
import ru.mertsalovda.core_api.database.entity.Value

class ValueAdapter(private val onClickListener: ((Value) -> Unit)? = null) :
    RecyclerView.Adapter<ValueAdapter.ValueViewHolder>() {

    private var items = listOf<Value>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ValueViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_value, parent, false)
        val binding = ItemValueBinding.bind(view)
        return ValueViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ValueViewHolder, position: Int) {
        holder.bind(items[position], onClickListener)
    }

    override fun getItemCount(): Int = items.size

    fun setData(data: List<Value>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean {
                val old = items[oldPos].code + items[oldPos].name
                val new = data[newPos].code + data[newPos].name
                return old == new
            }

            override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean =
                items[oldPos].hashCode() == data[newPos].hashCode()

            override fun getOldListSize(): Int = items.size
            override fun getNewListSize(): Int = data.size
        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)

        items = data
        diffResult.dispatchUpdatesTo(this)
    }


    class ValueViewHolder(private val binding: ItemValueBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("CheckResult")
        fun bind(item: Value, onClickListener: ((Value) -> Unit)?) {
            binding.materialTextView.text = "${item.code} - ${item.name}"
            itemView.setOnClickListener {
                onClickListener?.let { listener -> listener(item) }
            }

            if (item is Value.Currency) {
                // Загружаю изображение .svg
                GlideApp.with(itemView.context.applicationContext)
                    .`as`(PictureDrawable::class.java)
                    .transition(withCrossFade())
                    .error(R.drawable.ic_launcher_foreground)
                    .load(item.image)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .listener(SvgSoftwareLayerSetter())
                    .into(binding.imageView)
            } else {
                GlideApp.with(itemView.context.applicationContext)
                    .load(item.image?.toIntOrNull())
                    .error(R.drawable.ic_launcher_foreground)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(binding.imageView)
            }
        }
    }
}