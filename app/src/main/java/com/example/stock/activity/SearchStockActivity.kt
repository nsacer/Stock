package com.example.stock.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.stock.BaseActivity
import com.example.stock.Constant
import com.example.stock.R
import com.example.stock.RetrofitUtils
import com.example.stock.adapter.AdapterStockSearchResult
import com.example.stock.model.stockSearch.StockSearchResponse
import com.example.stock.utils.SharedPreferencesUtils
import kotlinx.android.synthetic.main.activity_search_stock.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 股票搜索页面
 * */
class SearchStockActivity : BaseActivity() {

    private lateinit var mAdapterSearch: AdapterStockSearchResult

    companion object {

        fun openAct(context: Context) {
            context.startActivity(Intent(context, SearchStockActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_stock)
    }

    override fun initView() {

        initToolbar()

        initRecyclerViewResult()
    }

    private fun initToolbar() {

        toolbarSearchStock.setNavigationOnClickListener { onBackPressed() }

        searchViewSearchStock.setIconifiedByDefault(false)
        searchViewSearchStock.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return if (newText.isNullOrEmpty()) {
                    mAdapterSearch.setList(null)
                    false
                } else {
                    searchStock(newText)
                    true
                }
            }
        })
    }

    private fun initRecyclerViewResult() {

        mAdapterSearch = AdapterStockSearchResult()
        mAdapterSearch.setOnItemChildClickListener { _, view, position ->
            val stockCode = mAdapterSearch.data[position].stockCode
            if (SharedPreferencesUtils.containsStock(stockCode)) {
                SharedPreferencesUtils.delStock(stockCode)
                mAdapterSearch.animatorCM90(view)
            } else {
                SharedPreferencesUtils.addStock(stockCode)
                mAdapterSearch.animatorCM45(view)
            }
        }
        rvSearchStock.layoutManager = GridLayoutManager(this, 2)
        rvSearchStock.adapter = mAdapterSearch
    }

    //搜索股票
    private fun searchStock(content: String) {

        launch {

            RetrofitUtils.instance.getApiSearchStock()
                .searchStock(Constant.searchStockVersion, content)
                .enqueue(object : Callback<StockSearchResponse> {
                    override fun onResponse(
                        call: Call<StockSearchResponse>,
                        response: Response<StockSearchResponse>
                    ) {
                        if (response.isSuccessful &&
                            response.body()?.response?.stockList?.isNotEmpty() == true
                        ) {
                            mAdapterSearch.setList(response.body()!!.response.stockList)
                        } else {
                            mAdapterSearch.setList(null)
                        }
                    }

                    override fun onFailure(call: Call<StockSearchResponse>, t: Throwable) {
                        mAdapterSearch.setList(null)
                    }

                })
        }
    }
}