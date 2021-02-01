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
import com.example.stock.BaseFragment
import com.example.stock.R
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

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initView() {

        initToolbar()

        myLiveDataModel = ViewModelUtils.getViewModel(this, MyLiveDataModel::class.java)
        myLiveDataModel.mGestureHas.observe(this, {
            switchGestureMine.isChecked = it
        })

        initGesture()

        initFinger()
    }

    private fun initToolbar() {

        toolbarTabMine.inflateMenu(R.menu.tab_mine)
        toolbarTabMine.setOnMenuItemClickListener(this::onMenuItemClick)
    }

    private fun initGesture() {

        switchGestureMine.setOnClickListener {
            showToast(R.string.set)
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

    //清除密码确认弹窗
    private fun alertClearGesture() {

        AlertDialog.Builder(requireContext()).setMessage(R.string.confirmClearGesturePassword)
            .setPositiveButton(
                R.string.confirm
            ) { _, _ -> myLiveDataModel.setGesturePassword(null) }
            .setOnCancelListener {
                switchGestureMine.isChecked = myLiveDataModel.mGestureHas.value ?: false
            }
            .create()
            .show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_set_tab_mine) {
            showToast(R.string.set)
            return true
        }
        return false
    }
}