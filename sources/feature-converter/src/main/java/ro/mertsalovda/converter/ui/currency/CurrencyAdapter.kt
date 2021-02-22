package ro.mertsalovda.converter.ui.currency

import android.graphics.drawable.PictureDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import ro.mertsalovda.converter.R
import ro.mertsalovda.converter.databinding.ItemCurrencyBinding
import ro.mertsalovda.converter.utils.GlideApp
import ro.mertsalovda.converter.utils.SvgSoftwareLayerSetter
import ru.mertsalovda.core_api.database.entity.CurrencyItem

class CurrencyAdapter(private val onClickListener: ((CurrencyItem) -> Unit)? = null) :
    RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    private var items = listOf<CurrencyItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_currency, parent, false)
        val binding = ItemCurrencyBinding.bind(view)
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bind(items[position], onClickListener)
    }

    override fun getItemCount(): Int = items.size

    fun setData(data: List<CurrencyItem>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean {
                val old = items[oldPos].currencyCode + items[oldPos].countryName
                val new = data[newPos].currencyCode + data[newPos].countryName
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


    class CurrencyViewHolder(private val binding: ItemCurrencyBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CurrencyItem, onClickListener: ((CurrencyItem) -> Unit)?) {
            binding.materialTextView.text = "${item.currencyCode} - ${item.countryName}"
            itemView.setOnClickListener {
                onClickListener?.let { listener -> listener.invoke(item) }
            }

            // Загружаю изображение .svg
            GlideApp.with(itemView.context)
                .`as`(PictureDrawable::class.java)
                .transition(withCrossFade())
                .error(R.drawable.ic_launcher_foreground)
                .load(item.flagUrl)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .listener(SvgSoftwareLayerSetter())
                .into(binding.imageView)
        }
    }
}