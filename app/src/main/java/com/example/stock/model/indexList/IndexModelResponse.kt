package com.example.stock.model.indexList

data class IndexModelResponse(
    val error_code: Int,
    val reason: String?,
    val result: Result?
)