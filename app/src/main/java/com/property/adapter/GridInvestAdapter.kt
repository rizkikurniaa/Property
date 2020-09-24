package com.property.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iss.method.CurrencyFormatter
import com.property.BuildConfig
import com.property.R
import com.property.databinding.ItemGridInvestPropertyBinding
import com.property.model.DataProperty

class GridInvestAdapter: RecyclerView.Adapter<GridInvestAdapter.GridViewHolder>() {
    private val dataProperty = ArrayList<DataProperty>()

    fun setData(items: ArrayList<DataProperty>) {
        dataProperty.addAll(items)
        notifyDataSetChanged()
    }

    fun clearData() {
        dataProperty.clear()
    }

    inner class GridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemGridInvestPropertyBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_grid_invest_property,
                parent,
                false
            )
        return GridViewHolder(view)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val data = dataProperty[position]
        val currencyFormatter = CurrencyFormatter()
        val maxCapital: Double = data.investmentCapital!!.toDouble()
        val currentCapital: Double = data.totalInvest!!.toDouble()

        var result: Double = (currentCapital/maxCapital) * 100

        with(holder) {
            Glide.with(itemView.context)
                .load(BuildConfig.BASE_IMG_PROPERTY + data.propertyImg)
                .placeholder(R.drawable.imgplaceholder)
                .into(binding.ivProperty)

            binding.tvPropertyName.text = data.propertyName
            binding.tvInstallmentCapital.text = currencyFormatter.currencyFormatter(data.investmentCapital)
            binding.progressBar.progress = result.toInt()
            binding.tvPercent.text = "${String.format("%.2f", result)} %"

//            itemView.setOnClickListener {
//
//                val context: Context = it!!.context
//                val intent = Intent(context, DetailPropertyActivity::class.java)
//                intent.putExtra(DetailPropertyActivity.EXTRA_PROPERTY, data)
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                context.startActivity(intent)
//
//            }
        }
    }

    override fun getItemCount(): Int {
        return dataProperty.size
    }
}