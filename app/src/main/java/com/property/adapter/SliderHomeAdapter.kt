package com.property.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.property.BuildConfig
import com.property.R
import com.property.databinding.ItemListSliderBinding
import com.property.model.DataSlider

class SliderHomeAdapter: RecyclerView.Adapter<SliderHomeAdapter.ListViewHolder>() {
    private val dataSlider = ArrayList<DataSlider>()

    fun setData(items: ArrayList<DataSlider>) {
        dataSlider.clear()
        dataSlider.addAll(items)
        notifyDataSetChanged()
    }

    fun clearData() {
        dataSlider.clear()
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemListSliderBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_slider, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val dataSlider = dataSlider[position]

        with(holder) {
            Glide.with(itemView.context)
                .load(BuildConfig.BASE_IMG_SLIDER + dataSlider.sliderImg)
                .into(binding.ivSlider)
        }
    }

    override fun getItemCount(): Int {
        return dataSlider.size
    }
}