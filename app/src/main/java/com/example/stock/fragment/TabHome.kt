package com.example.stock.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stock.BaseFragment
import com.example.stock.Constant
import com.example.stock.R
import com.example.stock.RetrofitUtils
import com.example.stock.activity.WebViewActivity
import com.example.stock.adapter.AdapterIndexList
import com.example.stock.model.indexList.Data
import com.example.stock.model.indexList.IndexModelResponse
import kotlinx.android.synthetic.main.fragment_tab_home.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * 首页Fragment
 */
class TabHome : BaseFragment() {

    private lateinit var mAdapter: AdapterIndexList
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
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_home, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TabHome().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun initView() {

        mAdapter = AdapterIndexList(mutableListOf())
        mAdapter.setOnItemClickListener { adapter, view, position ->
            val url = mAdapter.data[position].url
            if (url.isNullOrEmpty()) return@setOnItemClickListener
            WebViewActivity.openAct(requireContext(), url)
        }
        rvHome.layoutManager = LinearLayoutManager(requireContext())
        rvHome.adapter = mAdapter

        swipeRefreshHome.setOnRefreshListener { requestData() }
        swipeRefreshHome.isRefreshing = true
        requestData()
    }

    private fun requestData() {

        launch {

            RetrofitUtils.instance.getApiIndex()
                .getIndexDataPost(Constant.newTypeTop, "a3214edea3ef02245764da1d4c793769")
                .enqueue(object : Callback<IndexModelResponse> {
                    override fun onResponse(
                        call: Call<IndexModelResponse>,
                        response: Response<IndexModelResponse>
                    ) {
                        swipeRefreshHome.isRefreshing = false
                        if (response.body()?.result?.data?.isNotEmpty() == true) {
                            val newList = mutableListOf<Data>()
                            newList.addAll(response.body()!!.result!!.data!!)
                            mAdapter.setNewInstance(newList)
                        }
                    }

                    override fun onFailure(call: Call<IndexModelResponse>, t: Throwable) {
                        swipeRefreshHome.isRefreshing = false
                    }
                })
        }
    }
}