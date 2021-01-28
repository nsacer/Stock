package com.example.stock.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.stock.ApplicationStock;


public class SharedPreferencesUtils {

    private static SharedPreferences sp;
    public static final String SP_NAME = "com.zpf.stock";
    //存储的deviceID
    public static final String DEVICE_ID = "device_id";
    //本地存储的手势密码string
    public static final String GESTURE = "gesturePassword";
    //主题样式是否是夜间模式
    public static final String THEME_DARK = "themeDark";
    //本地存储的账号string
    public static final String ACCOUNT = "account";
    public static final String PASSWORD = "password";
    //本地存储的用户手机号string
    public static final String PHONE = "phone";
    //用户的UserId,string
    public static final String USER_ID = "user_id";
    //customerId
    public static final String CUSTOMER_ID = "customerId";
    //实名认证
    public static final String REALNAME = "realname";
    //是否可以手势密码登录提示string
    public static final String CAN_LOGIN_GESTURE_HINT = "canGestureLoginHint";
    //是否设置了指纹登陆boolean
    public static final String CAN_LOGIN_FINGER = "canLoginFinger";
    //上次人工视频双录成功的videoId
    public static final String VIDEO_ID = "videoId";

    public static SharedPreferences getInstance() {
        if (sp == null) {
            sp = ApplicationStock.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return sp;
    }

    public static void saveString(String key, String value) {

        getInstance().edit().putString(key, value).apply();
    }

    public static String getString(String key, String defValue) {

        return getInstance().getString(key, defValue);
    }

    //加密内容后存储
    public static void saveStringConvert(String key, String value) {
        getInstance().edit().putString(key, Md5Utils.convertMD5(value)).apply();
    }

    //获取解密后的内容
    public static String getStringConvert(String key, String defValue) {
        String result = getInstance().getString(key, defValue);
        return result.equals(defValue) ? defValue : Md5Utils.convertMD5(result);
    }

    public static void saveBoolean(String key, boolean value) {

        getInstance().edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key, boolean defValue) {

        return getInstance().getBoolean(key, defValue);
    }

    public static void saveInt(String key, int value) {

        getInstance().edit().putInt(key, value).apply();
    }

    public static int getInt(String key, int defValue) {

        return getInstance().getInt(key, defValue);
    }

    public static void saveFloat(String key, float value) {

        getInstance().edit().putFloat(key, value).apply();
    }

    public static float getFloat(String key, float defValue) {

        return getInstance().getFloat(key, defValue);
    }

    //是否存储有该key的内容
    public static boolean contains(String key) {
        return getInstance().contains(key);
    }

    //删除该key的内容
    public static void deleteByKey(String key) {
        if (contains(key)) {
            getInstance().edit().remove(key).apply();
        }
    }

}

