package com.example.stock.model.stockSearch

data class Response(
    val responseStatus: ResponseStatus,
    val stockList: List<Stock>
)