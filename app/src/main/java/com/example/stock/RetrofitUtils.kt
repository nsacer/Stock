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

    fun getApiSearchStock(): InterfaceRequestApi {
        val retrofit = getRetrofit(BASE_URL_SEARCH)
        return retrofit.create(InterfaceRequestApi::class.java)
    }

    companion object {

        //TODO 网络请求的 BASE_URL
        private const val BASE_URL_INDEX = "https://v.juhe.cn"
        //搜索股票的baseUrl
        private const val BASE_URL_SEARCH = "http://searchstock.9666.cn"
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

        @Synchronized
        private fun getRetrofit(baseUrl: String): Retrofit {

            val interceptor = HttpLoggingInterceptor { message -> Log.d("xxx", message) }
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val ok = OkHttpClient.Builder().addInterceptor(interceptor)
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
            if (retrofit == null) {
                retrofit = Retrofit.Builder().baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .build()
            }
            return retrofit!!
        }
    }
}