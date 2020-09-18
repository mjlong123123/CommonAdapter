package com.dragon.commonadapter.list.datasource

import com.dragon.commonadapter.collection.GroupList
import com.dragon.commonadapter.list.BaseAdapter
import com.dragon.commonadapter.list.PageLoader
import com.dragon.commonadapter.list.item.LoadingItem

/**
 * @author dragon
 */
class PagingDataSource<T : BaseAdapter.BaseItem, R : PageLoader.PreviousNextContainer>(
    private val header: LoadingItem? = null,
    private val footer: LoadingItem? = null
) : BaseAdapter.BaseDataSource<T>() {

    private val list = GroupList<T>()

    override fun get(index: Int) = list[index]

    override fun size() = list.size

    fun addGroup(group: Int, item: T) = list.addGroupData(group, item)

    fun clearGroup(group: Int) {
        list.clearGroup(group)
    }

    fun updateHeaderOrFooter(result: PageLoader.PageResult<R>) {
        when (result.parameter) {
            is PageLoader.Parameter.INIT -> {
                when (result) {
                    is PageLoader.PageResult.Success -> {
                        list.clearGroup(Int.MIN_VALUE)
                        list.clearGroup(Int.MAX_VALUE)
                        if (!result.data.previousKey().isNullOrEmpty() && header != null) {
                            list.addGroupData(Int.MIN_VALUE, header as T)
                        }
                        if (!result.data.nextKey().isNullOrEmpty() && footer != null) {
                            list.addGroupData(Int.MAX_VALUE, footer as T)
                        }
                    }
                }
            }
            is PageLoader.Parameter.NEXT -> {
                when (result) {
                    is PageLoader.PageResult.Success -> {
                        if (result.data.nextKey().isNullOrEmpty()) {
                            list.clearGroup(Int.MAX_VALUE)
                        }
                    }
                }
            }
            is PageLoader.Parameter.PREVIOUS -> {
                when (result) {
                    is PageLoader.PageResult.Success -> {
                        if (result.data.previousKey().isNullOrEmpty()) {
                            list.clearGroup(Int.MIN_VALUE)
                        }
                    }
                }
            }
        }
        header?.updateLoadState(result)
        footer?.updateLoadState(result)
    }
}