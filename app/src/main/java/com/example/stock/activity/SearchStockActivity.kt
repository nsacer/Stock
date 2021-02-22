package com.example.stock.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.appcompat.widget.SearchView
import com.example.stock.BaseActivity
import com.example.stock.Constant
import com.example.stock.R
import com.example.stock.RetrofitUtils
import com.example.stock.model.stockSearch.StockSearchResponse
import kotlinx.android.synthetic.main.activity_search_stock.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 股票搜索页面
 * */
class SearchStockActivity : BaseActivity() {

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
    }

    private fun initToolbar() {

        toolbarSearchStock.setNavigationOnClickListener { onBackPressed() }

        searchViewSearchStock.inputType = InputType.TYPE_CLASS_NUMBER
        searchViewSearchStock.setIconifiedByDefault(false)
        searchViewSearchStock.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return if (newText.isNullOrEmpty()) false
                else {
                    searchStock(newText)
                    true
                }
            }
        })
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
                            //TODO 填充数据
                            logInfo(response.body()!!.response.stockList.size.toString())
                        }
                    }

                    override fun onFailure(call: Call<StockSearchResponse>, t: Throwable) {
                        //TODO 没有结果
                    }

                })
        }
    }
}