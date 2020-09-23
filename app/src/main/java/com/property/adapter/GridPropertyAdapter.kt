package com.property.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iss.method.CurrencyFormatter
import com.property.BuildConfig
import com.property.R
import com.property.activity.DetailPropertyActivity
import com.property.databinding.ItemGridPropertyBinding
import com.property.model.DataProperty

class GridPropertyAdapter: RecyclerView.Adapter<GridPropertyAdapter.ListViewHolder>() {
    private val dataProperty = ArrayList<DataProperty>()

    fun setData(items: ArrayList<DataProperty>) {
        dataProperty.addAll(items)
        notifyDataSetChanged()
    }

    fun clearData() {
        dataProperty.clear()
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemGridPropertyBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_grid_property, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = dataProperty[position]
        val currencyFormatter = CurrencyFormatter()

        with(holder) {
            Glide.with(itemView.context)
                .load(BuildConfig.BASE_IMG_PROPERTY + data.propertyImg)
                .placeholder(R.drawable.imgplaceholder)
                .into(binding.ivProperty)

            binding.tvPropertyName.text = data.propertyName
            binding.tvInstallment.text = currencyFormatter.currencyFormatter(data.installment)
            binding.tvLocation.text = data.location

            itemView.setOnClickListener {

                val context: Context = it!!.context
                val intent = Intent(context, DetailPropertyActivity::class.java)
                intent.putExtra(DetailPropertyActivity.EXTRA_PROPERTY, data)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)

            }
        }
    }

    override fun getItemCount(): Int {
        return dataProperty.size
    }
}