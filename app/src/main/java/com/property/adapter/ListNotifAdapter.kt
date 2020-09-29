package com.property.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.R
import com.property.databinding.ItemRowNotificationBinding
import com.property.model.DataNotification

class ListNotifAdapter : RecyclerView.Adapter<ListNotifAdapter.ListViewHolder>() {
    private val dataNotification = ArrayList<DataNotification>()

    fun setData(items: ArrayList<DataNotification>) {
        dataNotification.clear()
        dataNotification.addAll(items)
        notifyDataSetChanged()
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemRowNotificationBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row_notification, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = dataNotification[position]

        with(holder) {
            binding.tvTitle.text = data.title
            binding.tvDateTime.text = "${data.date}, ${data.time}"
            binding.tvMessage.text = data.message

            if (position == dataNotification.size.minus(1)) {
                binding.view.visibility = View.GONE
            }

            itemView.setOnClickListener {

            }
        }
    }

    override fun getItemCount(): Int {
        return dataNotification.size
    }
}