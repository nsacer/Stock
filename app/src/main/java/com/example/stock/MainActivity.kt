package com.example.stock

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.RadioButton
import com.example.stock.activity.LoginActivity
import com.example.stock.fragment.TabHome
import com.example.stock.fragment.TabMine
import com.example.stock.fragment.TabStock
import com.example.stock.model.MyLiveDataModel
import com.example.stock.utils.SystemUtil
import com.example.stock.utils.ViewModelUtils
import com.github.jokar.permission.PermissionUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), Handler.Callback {

    //记录按下返回键的时间
    private var mTimeKeyDown: Long = 0
    private var mListTab: MutableList<BaseFragment> = mutableListOf()
    private lateinit var mTags: Array<String>
    private lateinit var mLiveDataModel: MyLiveDataModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun initData() {
        super.initData()
        mLiveDataModel = ViewModelUtils.getViewModel(this, MyLiveDataModel::class.java)
        mLiveDataModel.mIsLogin.observe(this, {
            if (!it) {
                logInfo("没有登录")
            }
        })
        mLiveDataModel.mNeedRecreate.observe(this, {
            if (it) recreate()
        })
    }

    override fun initView() {

        requestPermissions()

        getDeviceInfo()

        initTab()
    }

    //三方框架获取权限处理
    private fun requestPermissions() {

        PermissionUtil.Builder(this)
            .setPermissions(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_SMS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .setDenied {
                showToast("没有权限")
            }
            .setGrant {
                showToast("有权限")
            }
            .setNeverAskAgain {

            }
            .request()
    }

    //获取设备信息
    private fun getDeviceInfo() {

        Constant.deviceId = SystemUtil.getUUID()
    }

    private fun initTab() {

        mTags = resources.getStringArray(R.array.tabName)
        mListTab.add(TabHome.newInstance("", ""))
        mListTab.add(TabStock.newInstance("", ""))
        mListTab.add(TabMine.newInstance("", ""))

        rgTab.setOnCheckedChangeListener { _, checkedId ->
            dealWithFragment(checkedId)
        }
        dealWithFragment(R.id.rbHome)
    }

    private fun dealWithFragment(checkedId: Int) {

        var curr = 0
        resources.getStringArray(R.array.tabName).forEachIndexed { index, s ->
            if (s == findViewById<RadioButton>(checkedId).text) curr = index
        }

        if (curr == resources.getStringArray(R.array.tabName).size - 1 &&
            ViewModelUtils.getViewModel(this, MyLiveDataModel::class.java)
                .mIsLogin.value == false
        ) {
            LoginActivity.openLoginAct(this)
            (rgTab.getChildAt(rgTab.tag as Int) as RadioButton).isChecked = true
            return
        }

        val transition = supportFragmentManager.beginTransaction()
        mTags.forEachIndexed { _, s ->
            if (supportFragmentManager.findFragmentByTag(s) != null) {
                transition.hide(supportFragmentManager.findFragmentByTag(s)!!)
            }
        }
        if (supportFragmentManager.findFragmentByTag(mTags[curr]) != null) {
            transition.show(supportFragmentManager.findFragmentByTag(mTags[curr])!!).commit()
        } else {
            transition.add(R.id.flRoot, mListTab[curr], mTags[curr]).commit()
        }

        rgTab.tag = curr
    }

    override fun onBackPressed() {
        exitApp()
    }

    //退出App
    private fun exitApp() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - mTimeKeyDown > 2000) {
            showToast(R.string.press_again_exit)
            mTimeKeyDown = currentTime
        } else {
            finish()
        }
    }


    override fun handleMessage(msg: Message): Boolean {

        return false
    }
}