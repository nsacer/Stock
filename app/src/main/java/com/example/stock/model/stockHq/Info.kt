package com.example.stock.model.stockHq

data class Info(
    val currentPrice: String,
    val pxChg: String?,
    val pxChgRatio: String?,
    val stockCode: String,
    val stockName: String,
    val tradingStatus: String
) {

    fun getStockStatus(): String {

        return when (tradingStatus) {
            "103" -> "正常"
            "2" -> "停牌"
            "902" -> "已退市"
            "104" -> "未上市"
            "-2" -> "未开盘"
            "-1" -> "集合竞价"
            else -> "未知"
        }
    }
}