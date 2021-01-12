package com.example.stock


import com.example.stock.model.indexList.IndexModelResponse
import retrofit2.Call
import retrofit2.http.*

/**
 *
 * @ClassName:      InterfaceRequestApi
 * @Description:
 * @Author:         leeeeef
 * @CreateDate:     2019/12/24 17:11
 */
interface InterfaceRequestApi {

    //get请求
    @GET("/toutiao/index?type={zzzz}&key=a3214edea3ef02245764da1d4c793769")
    fun getIndexData(@Path("zzzz") type: String): Call<IndexModelResponse>
    //get请求
    @FormUrlEncoded
    @POST("/toutiao/index")
    fun getIndexDataPost(@Field("type") type: String, @Field("key") key: String): Call<IndexModelResponse>

    //post请求，form表单提交请求参数（id, page）
    @FormUrlEncoded
    @POST("http://v.juhe.cn/toutiao/index")
    fun getIndexHotData(@Field("type") id: Int, @Field("key") page: Int): Call<IndexModelResponse>
}