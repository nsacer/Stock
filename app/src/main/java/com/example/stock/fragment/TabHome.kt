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
import com.example.stock.adapter.AdapterIndexList
import com.example.stock.model.indexList.Data
import com.example.stock.model.indexList.IndexModelResponse
import kotlinx.android.synthetic.main.fragment_tab_home.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * create an instance of this fragment.
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

    override fun onResume() {
        super.onResume()
        logInfo("resume")
    }

    override fun onPause() {
        super.onPause()
        logInfo("pause")
    }

    override fun initView() {

        mAdapter = AdapterIndexList(mutableListOf())
        rvHome.layoutManager = LinearLayoutManager(requireContext())
        rvHome.adapter = mAdapter

        swipeRefreshHome.setOnRefreshListener {
            requestData()
        }
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
                        logInfo(response.body()?.reason)
                        swipeRefreshHome.isRefreshing = false
                        if (response.body()?.result?.data?.isNotEmpty() == true) {
                            val newList = mutableListOf<Data>()
                            newList.addAll(response.body()!!.result!!.data!!)
                            mAdapter.setNewInstance(newList)
                        }
                    }

                    override fun onFailure(call: Call<IndexModelResponse>, t: Throwable) {
                        logInfo(t.message)
                        swipeRefreshHome.isRefreshing = false
                    }
                })
        }
    }
}