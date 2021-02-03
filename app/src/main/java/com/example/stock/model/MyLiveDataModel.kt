package com.example.stock.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stock.utils.SharedPreferencesUtils

/**
 * 用户信息相关的model
 * */
class MyLiveDataModel : ViewModel() {

    //用户是否登录
    val mIsLogin: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(
            !SharedPreferencesUtils
                .getString(SharedPreferencesUtils.ACCOUNT, "").isNullOrEmpty()
        )
    }

    //是否需要重建

    //是否有手势密码
    val mGestureHas: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(
            !SharedPreferencesUtils
                .getString(SharedPreferencesUtils.GESTURE, null).isNullOrEmpty()
        )
    }

    //指纹密码
    val mFingerPwd: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(
            SharedPreferencesUtils
                .getBoolean(SharedPreferencesUtils.CAN_LOGIN_FINGER, false)
        )
    }

    //用户的头像地址
    val mAvatarUrl: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    //用户的账号
    val mAccount: MutableLiveData<String> by lazy {
        MutableLiveData<String>(
            SharedPreferencesUtils.getString(SharedPreferencesUtils.ACCOUNT, "")
        )
    }

    //用户的账号加*
    val mAccountSafe: MutableLiveData<String> by lazy {
        MutableLiveData<String>(
            SharedPreferencesUtils.getString(SharedPreferencesUtils.ACCOUNT_SAFE, "")
        )
    }

    //切换手势密码登录
    fun switchGesturePasswordLogin() {
        if (mGestureHas.value == true) {
            setGesturePassword(null)
        } else {
            //TODO
        }
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

    /**
     * 设置指纹登录
     * @param canLogin 是否可以指纹登录
     * */
    fun setFingerLogin(canLogin: Boolean) {
        SharedPreferencesUtils.saveBoolean(SharedPreferencesUtils.CAN_LOGIN_FINGER, canLogin)
    }

    /**
     * 更新用户的账号信息
     * @param account 用户的手机号
     * */
    fun updateUserAccount(account: String) {
        SharedPreferencesUtils.saveString(SharedPreferencesUtils.ACCOUNT, account)
        val accountSafeNew = account.replaceRange(3, 7, "****")
        SharedPreferencesUtils.saveString(SharedPreferencesUtils.ACCOUNT_SAFE, accountSafeNew)
        mAccount.value = account
        mAccountSafe.value = accountSafeNew
        mIsLogin.value = true
    }

    //退出登录
    fun logout() {
        SharedPreferencesUtils.deleteByKey(SharedPreferencesUtils.ACCOUNT)
        SharedPreferencesUtils.deleteByKey(SharedPreferencesUtils.ACCOUNT_SAFE)
        setGesturePassword(null)
        setFingerLogin(false)
        mIsLogin.value = false
    }

}