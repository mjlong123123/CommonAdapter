package com.dragon.commonadapter.list.datasource

import com.dragon.commonadapter.collection.GroupList
import com.dragon.commonadapter.list.BaseAdapter

/**
 * @author dragon
 */
class GroupedDataSource<T: BaseAdapter.BaseItem>: BaseAdapter.BaseDataSource<T>() {

    private val list = GroupList<T>()

    override fun get(index: Int) = list[index]

    override fun size() = list.size
    
    fun add(item:T) = list.add(item)
    fun addGroup(group:Int, item: T) = list.addGroupData(group, item)
    fun addGroup(group: Int, items:Collection<T>){
        list.addGroupAllData(group, items)
    }
    fun clearGroup(group: Int){
        list.clearGroup(group)
    }
}