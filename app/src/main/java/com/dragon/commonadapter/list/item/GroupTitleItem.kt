package com.dragon.commonadapter.list.item

import com.dragon.commonadapter.R
import com.dragon.commonadapter.list.BaseAdapter
import kotlinx.android.synthetic.main.item_group.view.*

/**
 * @author dragon
 */
class GroupTitleItem(
    val group: Int,
    private val title: String,
    private val action: (GroupTitleItem) -> Unit
) :
    BaseAdapter.BaseItem() {
    override val viewType: Int
        get() = R.layout.item_group

    override fun bind(holder: BaseAdapter.BaseViewHolder, position: Int) {
        holder.itemView.itemTitleView.text = title
        holder.itemView.itemDeleteView.setOnClickListener { action.invoke(this) }
    }
}