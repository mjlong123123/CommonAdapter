package com.dragon.commonadapter.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_ID
import kotlinx.android.extensions.LayoutContainer

/**
 * @author dragon
 */
class BaseAdapter<out T : BaseAdapter.BaseItem>(private val dataSource: BaseDataSource<T>) : RecyclerView.Adapter<BaseAdapter.BaseViewHolder>() {
    init {
        dataSource.attachedAdapter = this
    }

    override fun getItemViewType(position: Int) = dataSource.get(position).viewType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BaseViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))

    override fun getItemCount() = dataSource.size()

    override fun getItemId(position: Int) = dataSource.get(position).getStableId()

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = dataSource.get(position)
        item.viewHolder = holder
        holder.baseItem = item
        item.bind(holder, position)
    }

    abstract class BaseItem {
        internal var viewHolder: BaseViewHolder? = null
        val availableHolder: BaseViewHolder?
            get() {
                return if (viewHolder?.baseItem == this)
                    viewHolder
                else
                    null
            }
        abstract val viewType: Int
        abstract fun bind(holder: BaseViewHolder, position: Int)
        fun getStableId() = NO_ID
    }

    class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), LayoutContainer {
        var baseItem: BaseItem? = null
        override val containerView: View?
            get() = itemView
    }

    abstract class BaseDataSource<T : BaseItem> {
        var attachedAdapter: BaseAdapter<T>? = null
        abstract fun get(index: Int): T
        abstract fun size(): Int
    }
}