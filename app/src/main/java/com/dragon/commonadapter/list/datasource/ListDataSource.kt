package com.dragon.commonadapter.list.datasource

import com.dragon.commonadapter.list.BaseAdapter

/**
 * @author dragon
 */
class ListDataSource<T: BaseAdapter.BaseItem>: BaseAdapter.BaseDataSource<T>(){
    private val list = mutableListOf<T>()

    override fun get(index: Int) = list[index]

    override fun size() = list.size

    fun add(item:T){
        list.add(item)
    }
}