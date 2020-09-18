package com.dragon.commonadapter.list.item

import android.view.View
import com.dragon.commonadapter.R
import com.dragon.commonadapter.list.BaseAdapter
import kotlinx.android.synthetic.main.item_menu.view.*

/**
 * @author dragon
 */
class MenuItem(val title: String, callback: (MenuItem) -> Unit) : BaseAdapter.BaseItem() {
    override val viewType: Int
        get() = R.layout.item_menu

    private val eventListener = View.OnClickListener {
        callback.invoke(this)
    }

    override fun bind(holder: BaseAdapter.BaseViewHolder, position: Int) {
        holder.itemView.itemTitleView.text = title
        holder.itemView.setOnClickListener(eventListener)
    }
}