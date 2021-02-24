package com.example.stock.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stock.BaseFragment
import com.example.stock.Constant
import com.example.stock.R
import com.example.stock.RetrofitUtils
import com.example.stock.activity.SearchStockActivity
import com.example.stock.adapter.AdapterStockCornerList
import com.example.stock.model.stockHq.StockHQResponse
import com.example.stock.utils.SharedPreferencesUtils
import kotlinx.android.synthetic.main.fragment_tab_stock.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * 自选股列表Fragment
 */
class TabStock : BaseFragment(), Toolbar.OnMenuItemClickListener {

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mAdapterStocks: AdapterStockCornerList

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

    override fun onFirstVisible() {
        super.onFirstVisible()
        getLocalStockInfo()
    }

    override fun onVisible() {
        super.onVisible()
        getLocalStockInfo()
    }

    private fun getLocalStockInfo() {
        val stocks = SharedPreferencesUtils.getString(SharedPreferencesUtils.STOCK_LIST_LOCAL, "")
        if (stocks.isEmpty()) {
            mAdapterStocks.isUseEmpty = true
        } else {
            requestHQ(stocks)
        }
    }

    override fun initView() {

        initToolbar()

        initRecyclerView()
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

    private fun initRecyclerView() {

        mAdapterStocks = AdapterStockCornerList()
        mAdapterStocks.setEmptyView(R.layout.empty_no_cornor_stock)
        rvStockListTabStock.layoutManager = LinearLayoutManager(requireContext())
        rvStockListTabStock.adapter = mAdapterStocks
    }

    //请求股票的行情数据
    private fun requestHQ(stocks: String) {

        if (stocks.isEmpty()) return
        launch {
            RetrofitUtils.instance.getApiHQ()
                .stockHQ(Constant.platform, Constant.searchStockVersion, stocks)
                .enqueue(object : Callback<StockHQResponse> {
                    override fun onResponse(
                        call: Call<StockHQResponse>,
                        response: Response<StockHQResponse>
                    ) {
                        if (response.isSuccessful &&
                            response.body()?.response?.infos?.isNotEmpty() == true) {
                            mAdapterStocks.setList(response.body()!!.response.infos)
                        } else {
                            mAdapterStocks.setList(null)
                        }
                    }

                    override fun onFailure(call: Call<StockHQResponse>, t: Throwable) {

                        //TODO
                        logInfo(t.message)
                    }
                })
        }
    }
}