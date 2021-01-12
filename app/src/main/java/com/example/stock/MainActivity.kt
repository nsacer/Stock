package com.example.stock

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.KeyEvent
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.stock.fragment.TabHome
import com.example.stock.fragment.TabMine
import com.example.stock.fragment.TabStock
import com.example.stock.utils.SystemUtil
import com.github.jokar.permission.PermissionUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), Handler.Callback {

    //记录按下返回键的时间
    private var mTimeKeyDown: Long = 0
    private var mListTab: MutableList<BaseFragment> = mutableListOf()
    private lateinit var mTags: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initData()
    }

    private fun initData() {


    }

    private fun initView() {

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
                Toast.makeText(this, "no permission", Toast.LENGTH_SHORT).show()
            }
            .setGrant {
                Toast.makeText(this, "has permission", Toast.LENGTH_SHORT).show()
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
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && KeyEvent.ACTION_DOWN == event?.action) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - mTimeKeyDown > 2000) {
                Toast.makeText(this, R.string.press_again_exit, Toast.LENGTH_SHORT).show()
                mTimeKeyDown = currentTime
            } else {
                finish()
                exitProcess(0)
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun handleMessage(msg: Message): Boolean {

        return false
    }
}