package com.example.stock.utils

import androidx.appcompat.app.AppCompatActivity
import com.example.stock.R

class ThemeUtils {

    companion object {

        //设置页面主题样式
        fun setTheme(activity: AppCompatActivity) {

            if (SharedPreferencesUtils.getBoolean(SharedPreferencesUtils.THEME_DARK, false)) {
                activity.setTheme(R.style.Theme_Stock_Dark)
            }
        }
    }


}