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
    //隐藏部分信息的账号
    public static final String ACCOUNT_SAFE = "accountSafe";
    //是否设置了指纹登陆boolean
    public static final String CAN_LOGIN_FINGER = "canLoginFinger";
    //本地存储的股票列表
    public static final String STOCK_LIST_LOCAL = "stockListLocal";

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

    /**
     * 存储本地的股票列表是否包含该股票
     *
     * @param stockCode 股票代码
     */
    public static boolean containsStock(String stockCode) {
        if (stockCode == null || stockCode.isEmpty()) return true;
        String stockCodeList = getString(STOCK_LIST_LOCAL, "");
        return stockCodeList.contains(stockCode);
    }

    /**
     * 添加股票
     * 存储的格式：400900|400899|200100|
     *
     * @param stockCode 股票代码
     */
    public static void addStock(String stockCode) {
        if (stockCode == null || stockCode.isEmpty()) return;
        String stockCodeList = getString(STOCK_LIST_LOCAL, "");
        if (stockCodeList.contains(stockCode)) return;
        stockCodeList = stockCodeList + stockCode + "|";
        saveString(STOCK_LIST_LOCAL, stockCodeList);
    }

    /**
     * 删除本地存储的股票代码
     *
     * @param stockCode 要删除的股票代码
     */
    public static void delStock(String stockCode) {
        if (stockCode == null || stockCode.isEmpty()) return;
        String stockCodeList = getString(STOCK_LIST_LOCAL, "");
        if (stockCodeList.contains(stockCode)) {
            String stockListNew = stockCodeList.replace(stockCode + "|", "");
            saveString(STOCK_LIST_LOCAL, stockListNew);
        }
    }

}

