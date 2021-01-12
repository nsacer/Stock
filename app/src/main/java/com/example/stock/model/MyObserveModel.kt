package com.example.stock.model

import androidx.databinding.BaseObservable
import androidx.databinding.ObservableField

class MyObserveModel : BaseObservable() {

    val users = ObservableField<String>("default")

    fun changeUser() {
        users.set("zzzz")
        notifyChange()
    }
}
