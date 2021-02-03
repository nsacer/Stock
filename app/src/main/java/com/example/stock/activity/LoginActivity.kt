package com.example.stock.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.stock.BaseActivity
import com.example.stock.R
import com.example.stock.model.MyLiveDataModel
import com.example.stock.utils.UserUtil
import com.example.stock.utils.ViewModelUtils
import com.example.stock.utils.WxShareAndLoginUtils
import kotlinx.android.synthetic.main.activity_login.*

/**
 * 登录界面
 * */
class LoginActivity : BaseActivity() {

    companion object {

        fun openLoginAct(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun initView() {

        ivCloseLoginAct.setOnClickListener { onBackPressed() }
        tvPhoneNumLoginAct.setOnClickListener {
            val modelUser = ViewModelUtils.getViewModel(this, MyLiveDataModel::class.java)
            modelUser.updateUserAccount("15538391280")
            showToast(R.string.loginSuccess)
            finish()
        }
        ivWeChatLoginAct.setOnClickListener {
            WxShareAndLoginUtils.WxLogin(this)
        }
    }
}