package com.example.stock.model.stockHq

data class Response(
    val allowed: String,
    val infos: List<Info>,
    val responseStatus: ResponseStatus,
    val tradeDate: String
)