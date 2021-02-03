package com.example.stock.adapter

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.stock.R
import com.example.stock.model.indexList.Data

/**
 * 首页新闻列表Adapter
 * */
class AdapterIndexList(models: MutableList<Data>) :
    BaseQuickAdapter<Data, BaseViewHolder>(R.layout.item_index_news, models) {

    override fun convert(holder: BaseViewHolder, item: Data) {

        holder.setText(R.id.tvTitleIndexItem, item.title)
            .setText(R.id.tvAuthorIndexItem, item.author_name)
            .setText(R.id.tvTimeIndexItem, item.date)
        Glide.with(context)
            .load(item.thumbnail_pic_s)
            .into(holder.getView(R.id.ivCoverIndexItem))
    }
}