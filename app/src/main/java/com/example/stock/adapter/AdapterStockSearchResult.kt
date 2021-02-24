package com.example.stock.adapter

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.stock.R
import com.example.stock.model.stockSearch.Stock
import com.example.stock.utils.SharedPreferencesUtils

/**
 * 股票搜素结果列表
 * */
class AdapterStockSearchResult :
    BaseQuickAdapter<Stock, BaseViewHolder>(R.layout.item_stock_search_result) {

    init {
        addChildClickViewIds(R.id.ibAddStockSearchResultItem)
    }

    override fun convert(holder: BaseViewHolder, item: Stock) {

        holder.setText(R.id.tvStockNameSearchResultItem, item.stockName)
            .setText(R.id.tvStockCodeSearchResultItem, item.stockCode)
        if (!item.stockCode.isNullOrEmpty()) {
            val containStock = SharedPreferencesUtils.containsStock(item.stockCode)
            if (containStock) animatorCM45(holder.getView(R.id.ibAddStockSearchResultItem))
            else animatorCM90(holder.getView(R.id.ibAddStockSearchResultItem))
        }
    }

    fun animatorCM45(target: View) {
        val animator = ObjectAnimator.ofFloat(target, "rotation", 0f, 45f)
        animator.duration = 200
        animator.interpolator = DecelerateInterpolator()
        animator.start()
    }

    fun animatorCM90(target: View) {
        val animator = ObjectAnimator.ofFloat(target, "rotation", 0f, 90f)
        animator.duration = 200
        animator.interpolator = DecelerateInterpolator()
        animator.start()
    }
}