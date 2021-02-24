package com.example.stock

import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RetrofitUtils private constructor() {

    fun getApiIndex(): InterfaceRequestApi {
        val retrofit = getRetrofit(BASE_URL_INDEX)
        return retrofit.create(InterfaceRequestApi::class.java)
    }

    //获取搜索股票的Retrofit
    fun getApiSearchStock(): InterfaceRequestApi {
        val retrofit = getRetrofit(BASE_URL_SEARCH)
        return retrofit.create(InterfaceRequestApi::class.java)
    }

    //获取股票行情数据Retrofit
    fun getApiHQ(): InterfaceRequestApi {
        val retrofit = getRetrofit(BASE_URL_HQ)
        return retrofit.create(InterfaceRequestApi::class.java)
    }

    companion object {

        //聚合新闻
        private const val BASE_URL_INDEX = "https://v.juhe.cn"

        //搜索股票的baseUrl
        private const val BASE_URL_SEARCH = "http://searchstock.9666.cn"

        //行情数据
        private const val BASE_URL_HQ = "http://hq.9666.cn"
        private var retrofitUtils: RetrofitUtils? = null

        val instance: RetrofitUtils
            get() {

                if (retrofitUtils == null) {
                    synchronized(RetrofitUtils::class.java) {
                        if (retrofitUtils == null) {
                            retrofitUtils = RetrofitUtils()
                        }
                    }
                }
                return retrofitUtils!!
            }


        private var retrofit: Retrofit? = null

        //搜索股票
        private var retrofitCowboy: Retrofit? = null

        //行情数据
        private var retrofitHQ: Retrofit? = null

        @Synchronized
        private fun getRetrofit(baseUrl: String): Retrofit {

            val interceptor = HttpLoggingInterceptor { message -> Log.i("xxxx", message) }
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val okClient = OkHttpClient.Builder().addInterceptor(interceptor)
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
            return when (baseUrl) {
                BASE_URL_SEARCH -> {
                    if (retrofitCowboy == null) {
                        retrofitCowboy = Retrofit.Builder().baseUrl(baseUrl)
                            .client(okClient.build())
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(CoroutineCallAdapterFactory())
                            .build()
                    }
                    retrofitCowboy!!
                }
                BASE_URL_HQ -> {
                    if (retrofitHQ == null) {
                        retrofitHQ = Retrofit.Builder().baseUrl(baseUrl)
                            .client(okClient.build())
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(CoroutineCallAdapterFactory())
                            .build()
                    }
                    retrofitHQ!!
                }
                else -> {
                    if (retrofit == null) {
                        retrofit = Retrofit.Builder().baseUrl(baseUrl)
                            .client(okClient.build())
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(CoroutineCallAdapterFactory())
                            .build()
                    }
                    retrofit!!
                }
            }
        }
    }
}