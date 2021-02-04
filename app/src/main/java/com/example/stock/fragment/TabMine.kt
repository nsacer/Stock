package com.example.stock.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SwitchCompat
import com.bumptech.glide.Glide
import com.example.stock.BaseFragment
import com.example.stock.R
import com.example.stock.activity.GestureActivity
import com.example.stock.model.MyLiveDataModel
import com.example.stock.utils.FingerUtils
import com.example.stock.utils.ViewModelUtils
import kotlinx.android.synthetic.main.fragment_tab_mine.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TabMine : BaseFragment(), Toolbar.OnMenuItemClickListener {

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var myLiveDataModel: MyLiveDataModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tab_mine, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TabMine().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onVisible() {
        super.onVisible()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initView() {

        initToolbar()
        initHeader()
        initGesture()
        initFinger()
        initNightMode()
    }

    override fun initData() {
        super.initData()

        myLiveDataModel = ViewModelUtils.getViewModel(this, MyLiveDataModel::class.java)
        //用户头像信息
        myLiveDataModel.mAvatarUrl.observe(this, {
            Glide.with(requireContext())
                .load(it)
                .circleCrop()
                .placeholder(R.mipmap.app_start)
                .into(ivAvatarTabMine)
        })
        //账号信息
        myLiveDataModel.mAccountSafe.observe(this, {
            tvAccountTabMine.text = it
        })
        //手势登录开关
        myLiveDataModel.mGestureHas.observe(this, {
            switchGestureMine.isChecked = it
        })
        //指纹登录开关
        myLiveDataModel.mFingerPwd.observe(this, {
            switchFingerMine.isChecked = it
        })
        //夜间模式
        myLiveDataModel.mNightMode.observe(this, {
            switchNightModeMine.isChecked = it
        })
    }

    //头像账号昵称设置
    private fun initHeader() {

        Glide.with(requireContext()).load(resources.getStringArray(R.array.picUrlArray)[0])
            .circleCrop()
            .placeholder(R.mipmap.app_start)
            .into(ivAvatarTabMine)

        vLineAvaBtmLoginAct.setOnClickListener {
            showToast("跳转头像、昵称等设置页面")
        }
    }

    private fun initToolbar() {

        toolbarTabMine.inflateMenu(R.menu.tab_mine)
        toolbarTabMine.setOnMenuItemClickListener(this::onMenuItemClick)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_set_tab_mine) {
            alertExitAppDialog()
            return true
        }
        return false
    }

    //退出登录弹窗
    private fun alertExitAppDialog() {

        AlertDialog.Builder(requireContext())
            .setMessage(R.string.logout)
            .setPositiveButton(R.string.confirm) { dialog, _ ->
                myLiveDataModel.logout()
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    //手势密码设置
    private fun initGesture() {

        switchGestureMine.setOnClickListener {
            if (myLiveDataModel.mGestureHas.value == true) {
                alertClearGesture()
            } else {
                GestureActivity.startAct(requireContext())
                (it as SwitchCompat).isChecked = false
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initFinger() {

        switchFingerMine.setOnClickListener {
            if ((it as SwitchCompat).isChecked) {
                val dialog = AlertDialog.Builder(requireContext())
                    .setCancelable(false)
                    .create()
                val viewFinger = layoutInflater.inflate(R.layout.dialog_finger, null)
                viewFinger.findViewById<TextView>(R.id.tvCancelFinger).setOnClickListener {
                    dialog.dismiss()
                    FingerUtils.getInstance(requireContext()).cancelFingerAuth()
                }
                dialog.setView(viewFinger)
                dialog.show()
                FingerUtils.getInstance(requireContext())
                    .setFingerCheckListener(object : FingerUtils.OnFingerCheckListener {
                        override fun onCheckSuccess() {
                            showToast(R.string.success)
                        }

                        override fun onCheckFailed(message: String?) {
                            showToast(message)
                        }

                    })
                    .authFingerPrint()
            }
        }
    }

    //夜间模式
    private fun initNightMode() {

        switchNightModeMine.setOnClickListener { myLiveDataModel.switchNightMode() }
    }

    //清除密码确认弹窗
    private fun alertClearGesture() {

        AlertDialog.Builder(requireContext()).setMessage(R.string.confirmClearGesturePassword)
            .setPositiveButton(
                R.string.confirm
            ) { _, _ -> myLiveDataModel.setGesturePassword(null) }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .setOnDismissListener {
                switchGestureMine.isChecked = myLiveDataModel.mGestureHas.value ?: false
            }
            .create()
            .show()
    }
}