package com.example.stock.utils

class UserUtil {

    companion object {

        fun isLogin(): Boolean {
            return !SharedPreferencesUtils.getString(SharedPreferencesUtils.ACCOUNT,
                "").isNullOrEmpty()
        }

        fun saveUserInfo(account: String) {
            SharedPreferencesUtils.saveString(SharedPreferencesUtils.ACCOUNT, account)
            SharedPreferencesUtils.saveString(SharedPreferencesUtils.ACCOUNT_SAFE,
                account.replaceRange(3, 7, "****"))
        }
    }
}