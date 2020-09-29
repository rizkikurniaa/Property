package com.property.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.property.BuildConfig
import com.property.model.ResponseRegister
import org.json.JSONException
import org.json.JSONObject

class RegisterViewModel : ViewModel() {
    private val link: String = BuildConfig.BASE_API
    private val responseRegister = MutableLiveData<ResponseRegister>()

    fun requestLogin(email: String, phone: String, password: String) {

        AndroidNetworking.post(link + "register.php")
            .addBodyParameter("email", email)
            .addBodyParameter("no_telp", phone)
            .addBodyParameter("password", password)
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    try {
                        val responseData = ResponseRegister()

                        responseData.success = response.getInt("success")
                        responseData.message = response.getString("message")

                        responseRegister.postValue(responseData)

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

                override fun onError(anError: ANError) {

                }
            })
    }

    fun getResponseRegister(): LiveData<ResponseRegister> {
        return responseRegister
    }
}