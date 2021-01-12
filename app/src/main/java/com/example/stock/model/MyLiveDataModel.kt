package com.example.stock.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stock.utils.SharedPreferencesUtils

class MyLiveDataModel : ViewModel() {

    //是否有手势密码
    val mGestureHas: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(!SharedPreferencesUtils
            .getString(SharedPreferencesUtils.GESTURE, null).isNullOrEmpty())
    }

    /**
     * 设置手势密码
     * @param pwd 手势密码
     * */
    fun setGesturePassword(pwd: String?) {
        if (pwd.isNullOrEmpty()) {
            SharedPreferencesUtils.deleteByKey(SharedPreferencesUtils.GESTURE)
            mGestureHas.value = false
        } else {
            SharedPreferencesUtils.saveString(SharedPreferencesUtils.GESTURE, pwd)
            mGestureHas.value = true
        }
    }

}