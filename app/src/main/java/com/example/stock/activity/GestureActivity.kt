package com.example.stock.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.stock.BaseActivity
import com.example.stock.R
import com.example.stock.model.MyLiveDataModel
import com.example.stock.utils.SharedPreferencesUtils
import com.example.stock.utils.ViewModelUtils
import com.example.stock.view.gesture.GesturePassword
import kotlinx.android.synthetic.main.activity_gesture.*

class GestureActivity : BaseActivity() {

    //是否是第一次设置密码
    private var mIsFirstSetPwd = true
    //第一次绘制的密码
    private lateinit var mFirstPassword: String
    //本地存储密码
    private lateinit var mLocalPassword: String

    companion object {

        fun startAct(context: Context) {
            context.startActivity(Intent(context, GestureActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gesture)
    }

    override fun initView() {

        initGesture()
    }

    private fun initGesture() {

        mLocalPassword = SharedPreferencesUtils.getString(SharedPreferencesUtils.GESTURE, "")
        if (mLocalPassword.isNotEmpty()) tvHintGesture.text = getString(R.string.gestureCheck)

        gestureGestureAct.setOnCompleteListener(object : GesturePassword.OnCompleteListener {
            override fun onComplete(password: String?) {
                if (password.isNullOrEmpty()) return
                if (mLocalPassword.isNotEmpty()) {
                    if (password == mLocalPassword) {
                        //验证成功
                        tvHintGesture.text = getString(R.string.passwordRight)
                        finish()
                    } else {
                        //验证错误
                        tvHintGesture.text = getString(R.string.gestureCheckAgain)
                        gestureGestureAct.markError()
                    }
                } else {

                    if (mIsFirstSetPwd) {
                        gestureGestureAct.clearPassword()
                        mFirstPassword = password
                        mIsFirstSetPwd = false
                        tvHintGesture.text = getString(R.string.gestureInputAgain)
                    } else {
                        if (password == mFirstPassword) {
                            gestureGestureAct.clearPassword()
                            ViewModelUtils.getViewModel(this@GestureActivity,
                                MyLiveDataModel::class.java).setGesturePassword(password)
                            finish()
                        } else {
                            tvHintGesture.text = getString(R.string.gestureInputAgainNotSame)
                            gestureGestureAct.markError()
                        }
                    }
                }
            }

            override fun onPasswordTooShort() {
                tvHintGesture.setText(R.string.gestureTooShort)
                gestureGestureAct.clearPassword(0)
            }

        })
    }
}