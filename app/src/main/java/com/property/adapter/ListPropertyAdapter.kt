package com.property.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iss.method.CurrencyFormatter
import com.property.BuildConfig
import com.property.R
import com.property.databinding.ItemListPropertyBinding
import com.property.model.DataProperty

class ListPropertyAdapter: RecyclerView.Adapter<ListPropertyAdapter.ListViewHolder>() {
    private val dataProperty = ArrayList<DataProperty>()

    fun setData(items: ArrayList<DataProperty>) {
        dataProperty.addAll(items)
        notifyDataSetChanged()
    }

    fun clearData() {
        dataProperty.clear()
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemListPropertyBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_property, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = dataProperty[position]
        val currencyFormatter = CurrencyFormatter()

        with(holder) {
            Glide.with(itemView.context)
                .load(BuildConfig.BASE_IMG_PROPERTY + data.propertyImg)
                .into(binding.ivProperty)

            binding.tvPropertyName.text = data.propertyName
            binding.tvInstallment.text = currencyFormatter.currencyFormatter(data.installment)
            binding.tvLocation.text = data.location
        }
    }

    override fun getItemCount(): Int {
        return dataProperty.size
    }
}