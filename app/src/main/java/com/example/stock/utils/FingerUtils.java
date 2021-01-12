package com.example.stock.utils;

import android.app.KeyguardManager;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Handler;

import androidx.annotation.RequiresApi;

/**
 * 指纹识别工具类
 */
public class FingerUtils {

    private final KeyguardManager keyguardManager;
    private static FingerUtils singleton = null;
    //指纹识别结果监听接口
    private static OnFingerCheckListener mOnFingerCheckListener;
    //AndroidP版本之下的生物识别
    private final FingerprintManager fingerprintManager;
    //生物识别取消类
    private static CancellationSignal mCancelSignal;


    @RequiresApi(api = Build.VERSION_CODES.M)
    private FingerUtils(Context context) {
        fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
        keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static FingerUtils getInstance(Context context) {
        if (singleton == null) {
            synchronized (FingerUtils.class) {
                if (singleton == null) {
                    singleton = new FingerUtils(context);
                }
            }
        }
        return singleton;
    }

    /**
     * 检查手机硬件（有没有指纹感应区）
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean isHardFinger() {
        return fingerprintManager != null && fingerprintManager.isHardwareDetected();
    }

    /**
     * 检查手机是否开启锁屏密码
     */
    public boolean isWindowSafe() {
        return keyguardManager != null && keyguardManager.isKeyguardSecure();
    }

    /**
     * 检查手机是否已录入指纹
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean isHaveHandler() {
        return fingerprintManager != null && fingerprintManager.hasEnrolledFingerprints();
    }

    /**
     * 创建指纹验证
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void authenticate(FingerprintManager.CryptoObject cryptoObject,
                             CancellationSignal cancellationSignal,
                             int flag,
                             FingerprintManager.AuthenticationCallback authenticationCallback,
                             Handler handler) {
        if (fingerprintManager != null) {
            fingerprintManager.authenticate(cryptoObject, cancellationSignal, flag, authenticationCallback, handler);
        }
    }

    //
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void authFingerPrint() {
        //重新创建指纹识别取消类，如果使用同一个无法取消后再次开启
        mCancelSignal = new CancellationSignal();
        FingerprintManager.AuthenticationCallback mFingerCallBack = new FingerprintManager.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                if (mOnFingerCheckListener != null)
                    mOnFingerCheckListener.onCheckFailed(errString.toString());
            }

            @Override
            public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                super.onAuthenticationHelp(helpCode, helpString);
                if (mOnFingerCheckListener != null)
                    mOnFingerCheckListener.onCheckFailed(helpString.toString());
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                if (mOnFingerCheckListener != null) mOnFingerCheckListener.onCheckSuccess();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                if (mOnFingerCheckListener != null)
                    mOnFingerCheckListener.onCheckFailed("识别失败，请重试！");
            }
        };

        fingerprintManager.authenticate(null, mCancelSignal, 0, mFingerCallBack, null);
    }

    //取消指纹识别认证
    public void cancelFingerAuth() {
        if (mCancelSignal != null) mCancelSignal.cancel();
    }

    public FingerUtils setFingerCheckListener(OnFingerCheckListener listener) {
        mOnFingerCheckListener = listener;
        return singleton;
    }

    //指纹检查结果监听接口回调
    public interface OnFingerCheckListener {

        void onCheckSuccess();

        void onCheckFailed(String message);
    }
}

