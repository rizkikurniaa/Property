package com.property.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefManager(context: Context) {
    private var sp: SharedPreferences
    private var spEditor: SharedPreferences.Editor

    val SP_DATA_USER = "spDataUser"
    val SP_TOKEN = "spToken"
    val SP_ID = "spId"
    val SP_SUDAH_LOGIN = "spSudahLogin"

    init {
        sp = context.getSharedPreferences(SP_DATA_USER, Context.MODE_PRIVATE)
        spEditor = sp.edit()
        spEditor.apply()
    }

    fun saveSPString(keySP: String?, value: String?) {
        spEditor!!.putString(keySP, value)
        spEditor!!.commit()
    }

    fun saveSPInt(keySP: String?, value: Int) {
        spEditor!!.putInt(keySP, value)
        spEditor!!.commit()
    }

    fun saveSPBoolean(keySP: String?, value: Boolean) {
        spEditor!!.putBoolean(keySP, value)
        spEditor!!.commit()
    }

    fun getSpDataUser(): String? {
        return SP_DATA_USER
    }

    fun getSpToken(): String? {
        return sp.getString(SP_TOKEN, "")
    }

    fun getSpId(): String? {
        return sp.getString(SP_ID, "")
    }

    fun getSpSudahLogin(): Boolean? {
        return sp.getBoolean(SP_SUDAH_LOGIN, false)
    }

}