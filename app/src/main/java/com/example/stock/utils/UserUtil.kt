package com.example.stock.utils

class UserUtil {

    companion object {

        fun isLogin(): Boolean {
            return !SharedPreferencesUtils.getString(
                SharedPreferencesUtils.ACCOUNT,
                ""
            ).isNullOrEmpty()
        }

        fun saveUserInfo(account: String) {
            SharedPreferencesUtils.saveString(SharedPreferencesUtils.ACCOUNT, account)
            SharedPreferencesUtils.saveString(
                SharedPreferencesUtils.ACCOUNT_SAFE,
                account.replaceRange(3, 7, "****")
            )
        }

        //获取本地存储的账号
        fun account(): String {
            return SharedPreferencesUtils.getString(SharedPreferencesUtils.ACCOUNT, "")
        }

        //获取本地存储的加*的账号
        fun accountSafe(): String {
            return SharedPreferencesUtils.getString(SharedPreferencesUtils.ACCOUNT_SAFE, "")
        }
    }
}