package com.example.stock

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.stock.utils.ThemeUtils

abstract class BaseActivity : AppCompatActivity() {

    //是否执行了初始化方法
    private var mViewInit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.setTheme(this)
    }

    override fun onStart() {
        super.onStart()
        if (!mViewInit) {
            initData()
            initView()
            mViewInit = true
        }
    }

    open fun initData() {}

    abstract fun initView()

    fun showToast(content: String) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
    }

    fun showToast(resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
    }

    fun logInfo(content: String?) {
        Log.i("xxxx", "${javaClass.simpleName}:$content")
    }

    fun setFullScreen() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
    }
}