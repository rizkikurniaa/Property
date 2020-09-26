package com.property.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.R
import com.property.databinding.ItemRowSubdistrictBinding
import com.property.interfaceApp.PropertyBySubDistrict
import com.property.model.DataSubDistrict

class ListSubDistrictAdapter: RecyclerView.Adapter<ListSubDistrictAdapter.ListViewHolder>() {
    private val dataSubDistrict = ArrayList<DataSubDistrict>()
    private lateinit var propertyBySubDistrict: PropertyBySubDistrict

    fun setPropertyBySubDistrict(propertyBySubDistrict: PropertyBySubDistrict) {
        this.propertyBySubDistrict = propertyBySubDistrict
    }

    fun setData(items: ArrayList<DataSubDistrict>) {
        dataSubDistrict.clear()
        dataSubDistrict.addAll(items)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemRowSubdistrictBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_subdistrict, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = dataSubDistrict[position]

        with(holder) {
            binding.tvSubdistrict.text = data.subDistrict
            if (position == dataSubDistrict.size.minus(1)){
                binding.view.visibility = View.GONE
            }

            itemView.setOnClickListener {
                val subDistrictId = data.idSubDistrict
                propertyBySubDistrict.propertyBySubDistrict(subDistrictId.toString())
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSubDistrict.size
    }
}