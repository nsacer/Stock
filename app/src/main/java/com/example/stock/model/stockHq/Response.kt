package com.example.stock.model.stockHq

data class Response(
    val allowed: String?,
    val infos: List<Info>,
    val responseStatus: ResponseStatus,
    val tradeDate: String
) {
    //是否是开盘期间，可以轮询数据
    fun isCanService(): Boolean {
        return "1" == allowed
    }
}