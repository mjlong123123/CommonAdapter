package com.dragon.commonadapter.list.item

import com.dragon.commonadapter.list.BaseAdapter
import com.dragon.commonadapter.list.PageLoader

/**
 * @author dragon
 */
abstract class LoadingItem : BaseAdapter.BaseItem() {

    abstract fun updateLoadState(result: PageLoader.PageResult<*>?)
}