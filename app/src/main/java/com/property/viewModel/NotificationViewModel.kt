package com.property.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.property.BuildConfig
import com.property.model.DataNotification
import org.json.JSONException
import org.json.JSONObject

class NotificationViewModel : ViewModel() {
    private val link: String = BuildConfig.BASE_API
    private val listNotification = MutableLiveData<ArrayList<DataNotification>>()
    private val totPage = MutableLiveData<Int>()

    fun setNotification(token: String, page: Int) {
        val notif = ArrayList<DataNotification>()
        var totalPage: Int

        AndroidNetworking.post(link + "getNotifikasi.php?page=$page")
            .addHeaders("Authorization", "Bearer $token")
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.d("Success notification", "onResponse: $response")
                    run {
                        try {
                            val datalist = response.getJSONArray("data")
                            for (i in 0 until datalist.length()) {
                                val data = datalist.getJSONObject(i)
                                val notifItems = DataNotification()

                                notifItems.idNotification = data.getString("id_notifikasi")
                                notifItems.date = data.getString("tgl")
                                notifItems.time = data.getString("waktu")
                                notifItems.title = data.getString("judul")
                                notifItems.message = data.getString("isi")
                                notif.add(notifItems)
                            }

                            totalPage = response.getInt("total_page")

                            listNotification.postValue(notif)
                            totPage.postValue(totalPage)

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onError(error: ANError) {
                    Log.d("Error notification", "onError: $error")
                }
            })
    }

    fun getNotification(): LiveData<ArrayList<DataNotification>> {
        return listNotification
    }

    fun getTotalPage(): LiveData<Int> {
        return totPage
    }
}