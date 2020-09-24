package com.property.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.property.BuildConfig
import com.property.model.DataProperty
import com.property.model.DataSlider
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class HomeViewModel: ViewModel() {
    private val link: String = BuildConfig.BASE_API
    private val listSlider = MutableLiveData<ArrayList<DataSlider>>()
    private val listProperty = MutableLiveData<ArrayList<DataProperty>>()
    private val listInvest = MutableLiveData<ArrayList<DataProperty>>()

    fun setSlider() {
        val slider = ArrayList<DataSlider>()
        AndroidNetworking.get(link + "getSlider.php")
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                    Log.d("Success slider", "onResponse: $response")
                    run {
                        try {
                            for (i in 0 until response.length()) {
                                val data = response.getJSONObject(i)
                                val sliderItems = DataSlider()

                                sliderItems.idSlider = data.getString("id_slider")
                                sliderItems.sliderImg = data.getString("gambar_slider")

                                slider.add(sliderItems)

                            }

                            listSlider.postValue(slider)

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onError(error: ANError) {
                    Log.d("Error Slider", "onError: $error")
                }
            })
    }

    fun setProperty() {
        val property = ArrayList<DataProperty>()
        AndroidNetworking.get(link + "getProperti.php?per_page=4")
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.d("Success property", "onResponse: $response")
                    run {
                        try {
                            val datalist = response.getJSONArray("data")
                            for (i in 0 until datalist.length()) {
                                val data = datalist.getJSONObject(i)
                                val propertyItems = DataProperty()

                                propertyItems.idProperty = data.getString("id_properti")
                                propertyItems.propertyName = data.getString("nama_properti")
                                propertyItems.location = data.getString("lokasi")
                                propertyItems.subDistrict = data.getString("nama_daerah")
                                propertyItems.bedRoomQty = data.getString("kamar_tidur")
                                propertyItems.bathRoomQty = data.getString("kamar_mandi")
                                propertyItems.areaWide = data.getString("luas")
                                propertyItems.investmentCapital = data.getString("modal_investasi")
                                propertyItems.price = data.getString("harga_jual")
                                propertyItems.installment = data.getString("besar_angsuran")
                                propertyItems.longInstallments = data.getString("lama_angsuran")
                                propertyItems.propertyImg = data.getString("gambar_properti")
                                propertyItems.desc = data.getString("deskripsi")
                                propertyItems.totalInvest = data.getString("total_investasi")
                                property.add(propertyItems)
                            }

                            listProperty.postValue(property)

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onError(error: ANError) {
                    Log.d("Error Property", "onError: $error")
                }
            })
    }

    fun setInvest() {
        val invest = ArrayList<DataProperty>()
        AndroidNetworking.get(link + "getProperti.php?per_page=4&sort=asc")
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.d("Success invest", "onResponse: $response")
                    run {
                        try {
                            val datalist = response.getJSONArray("data")
                            for (i in 0 until datalist.length()) {
                                val data = datalist.getJSONObject(i)
                                val propertyItems = DataProperty()

                                propertyItems.idProperty = data.getString("id_properti")
                                propertyItems.propertyName = data.getString("nama_properti")
                                propertyItems.location = data.getString("lokasi")
                                propertyItems.subDistrict = data.getString("nama_daerah")
                                propertyItems.bedRoomQty = data.getString("kamar_tidur")
                                propertyItems.bathRoomQty = data.getString("kamar_mandi")
                                propertyItems.areaWide = data.getString("luas")
                                propertyItems.investmentCapital = data.getString("modal_investasi")
                                propertyItems.price = data.getString("harga_jual")
                                propertyItems.installment = data.getString("besar_angsuran")
                                propertyItems.longInstallments = data.getString("lama_angsuran")
                                propertyItems.propertyImg = data.getString("gambar_properti")
                                propertyItems.desc = data.getString("deskripsi")
                                propertyItems.totalInvest = data.getString("total_investasi")
                                invest.add(propertyItems)
                            }

                            listInvest.postValue(invest)

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onError(error: ANError) {
                    Log.d("Error Invest", "onError: $error")
                }
            })
    }

    fun getSlider(): LiveData<ArrayList<DataSlider>> {
        return listSlider
    }

    fun getProperty(): LiveData<ArrayList<DataProperty>> {
        return listProperty
    }

    fun getInvest(): LiveData<ArrayList<DataProperty>> {
        return listInvest
    }
}