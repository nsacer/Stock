package com.example.stock.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import com.example.stock.BaseFragment
import com.example.stock.R
import com.example.stock.activity.SearchStockActivity
import kotlinx.android.synthetic.main.fragment_tab_stock.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * 自选股列表Fragment
 */
class TabStock : BaseFragment(), Toolbar.OnMenuItemClickListener {

    private var param1: String? = null
    private var param2: String? = null

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
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_stock, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TabStock().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun initView() {

        initToolbar()

    }

    private fun initToolbar() {

        toolbarTabStock.setTitle(R.string.tab_stock)
        toolbarTabStock.inflateMenu(R.menu.tab_stock)
        toolbarTabStock.setOnMenuItemClickListener(this::onMenuItemClick)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_add_stock) {
            SearchStockActivity.openAct(requireContext())
            return true
        }
        return false
    }
}