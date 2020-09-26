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
import com.property.model.DataSubDistrict
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class AllHouseViewModel : ViewModel() {
    private val link: String = BuildConfig.BASE_API
    private val listProperty = MutableLiveData<ArrayList<DataProperty>>()
    private val listSubDistrict = MutableLiveData<ArrayList<DataSubDistrict>>()
    private val totPage = MutableLiveData<Int>()

    fun setProperty(subDistrict: String, page: Int) {
        val property = ArrayList<DataProperty>()
        var totalPage: Int
        var linkProperty: String = if (subDistrict == "All") {
            link + "getProperti.php?page=$page"
        } else {
            link + "getProperti.php?cari_daerah=$subDistrict&page=$page"
        }

        AndroidNetworking.get(linkProperty)
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
                                propertyItems.totalInvestor = data.getString("total_investor")
                                property.add(propertyItems)
                            }

                            totalPage = response.getInt("total_page")
                            listProperty.postValue(property)
                            totPage.postValue(totalPage)

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

    fun setSubDistrict() {
        val subDistrict = ArrayList<DataSubDistrict>()

        AndroidNetworking.get(link + "getDaerah.php")
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                    Log.d("Success Subdistrict", "onResponse: $response")
                    run {
                        try {
                            for (i in 0 until response.length()) {
                                val data = response.getJSONObject(i)
                                val subDistrictItems = DataSubDistrict()

                                subDistrictItems.idSubDistrict = data.getString("id_daerah")
                                subDistrictItems.subDistrict = data.getString("nama_daerah")
                                subDistrict.add(subDistrictItems)

                            }

                            listSubDistrict.postValue(subDistrict)

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onError(error: ANError) {
                    Log.d("Error Subdistrict", "onError: $error")
                }
            })
    }


    fun getProperty(): LiveData<ArrayList<DataProperty>> {
        return listProperty
    }

    fun getSubDistrict(): LiveData<ArrayList<DataSubDistrict>> {
        return listSubDistrict
    }

    fun getTotalPage(): LiveData<Int> {
        return totPage
    }
}