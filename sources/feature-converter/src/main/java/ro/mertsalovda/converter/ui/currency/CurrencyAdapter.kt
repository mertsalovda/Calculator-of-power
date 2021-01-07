package ro.mertsalovda.converter.ui.currency

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ro.mertsalovda.converter.R
import ru.mertsalovda.core_api.dto.Currency

class CurrencyAdapter(onClickListener: () -> Unit) : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    private var items = listOf<Currency>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_currency, parent, false)
        return CurrencyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun setData(data: List<Currency>) {
        items = data
        notifyDataSetChanged()
    }


    class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(currency: Currency) {

        }
    }
}