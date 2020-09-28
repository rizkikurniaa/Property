package com.property.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.property.BuildConfig
import com.property.model.DataLogin
import org.json.JSONException
import org.json.JSONObject

class LoginViewModel: ViewModel() {
    private val link: String = BuildConfig.BASE_API
    private val dataLogin = MutableLiveData<DataLogin>()

    fun requestLogin(email: String, password: String) {

        AndroidNetworking.post(link + "login.php")
            .addBodyParameter("email", email)
            .addBodyParameter("password", password)
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    try {
                        val login = DataLogin()

                        login.success = response.getInt("success")
                        login.token = response.getString("token")
                        login.message = response.getString("message")

                        dataLogin.postValue(login)

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

                override fun onError(anError: ANError) {

                }
            })
    }

    fun getUserLogin(): LiveData<DataLogin> {
        return dataLogin
    }
}