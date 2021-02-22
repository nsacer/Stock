package com.example.stock.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.stock.BaseActivity
import com.example.stock.R
import kotlinx.android.synthetic.main.activity_web_view.*

/**
 * H5页面
 * */
class WebViewActivity : BaseActivity() {

    //H5加载的url
    private lateinit var mUrlPage: String

    companion object {

        const val TAG_URL = "URL"

        fun openAct(context: Context, url: String) {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra(TAG_URL, url)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
    }

    override fun initData() {
        super.initData()
        mUrlPage = intent.getStringExtra(TAG_URL).toString()
    }

    override fun initView() {

        initToolbar()

        initWebView()
        webViewWeb.loadUrl(mUrlPage)

    }

    private fun initToolbar() {

        toolbarWebView.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initWebView() {

        webViewWeb.webViewClient = object : WebViewClient(){

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
//                return super.shouldOverrideUrlLoading(view, request)
                return false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (view?.contentHeight != 0) pbWebView.visibility = View.GONE
            }
        }

        webViewWeb.webChromeClient = object : WebChromeClient() {

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                pbWebView.progress = newProgress
                pbWebView.postInvalidate()
            }
        }
    }
}