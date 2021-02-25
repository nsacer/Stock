package com.example.stock.adapter

import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.stock.R
import com.example.stock.model.stockHq.Info
import java.math.BigDecimal

/**
 * 自选股列表adapter
 * */
class AdapterStockCornerList :
    BaseQuickAdapter<Info, BaseViewHolder>(R.layout.stock_tab_list_item) {

    override fun convert(holder: BaseViewHolder, item: Info) {

        holder.setText(R.id.tvStockNameListItem, item.stockName)
            .setText(R.id.tvStockCodeListItem, item.stockCode)
            .setText(R.id.tvPriceListItem, item.currentPrice)
            .setText(
                R.id.tvRatioListItem,
                if (item.pxChgRatio.isNullOrEmpty()) item.getStockStatus() else item.pxChgRatio
            )
        var colorStock = ContextCompat.getColor(context, R.color.stock_gray)
        if (!item.pxChgRatio.isNullOrEmpty()) {

            val bigRatio = BigDecimal(item.pxChgRatio)
            var strRatio = bigRatio.setScale(2, BigDecimal.ROUND_UP).toString()
            val dRatio = bigRatio.setScale(2, BigDecimal.ROUND_UP).toDouble()
            if (dRatio > 0) {
                strRatio = "+$strRatio"
                colorStock = ContextCompat.getColor(context, R.color.stock_red)
            } else if (dRatio < 0) {
                colorStock = ContextCompat.getColor(context, R.color.stock_green)
            }
            holder.setText(R.id.tvRatioListItem, "$strRatio%")
                .setBackgroundColor(R.id.tvRatioListItem, colorStock)
                .setTextColor(R.id.tvPriceListItem, colorStock)
        }
    }
}