package com.example.stock.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.stock.R
import com.example.stock.model.stockHq.Info

/**
 * 自选股列表adapter
 * */
class AdapterStockCornerList :
    BaseQuickAdapter<Info, BaseViewHolder>(R.layout.stock_tab_list_item) {

    override fun convert(holder: BaseViewHolder, item: Info) {

        holder.setText(R.id.tvStockNameListItem, item.stockName)
            .setText(R.id.tvStockCodeListItem, item.stockCode)
            .setText(R.id.tvPriceListItem, item.currentPrice)
        if (item.pxChg.isNullOrEmpty()) {
            holder.setText(R.id.tvRatioListItem, item.getStockStatus())
        } else {
            holder.setText(R.id.tvRatioListItem, item.pxChgRatio)
        }
    }
}