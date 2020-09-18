package com.dragon.commonadapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dragon.commonadapter.list.BaseAdapter
import com.dragon.commonadapter.list.datasource.GroupedDataSource
import com.dragon.commonadapter.list.item.GroupTitleItem
import com.dragon.commonadapter.list.item.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val dataSource = GroupedDataSource<BaseAdapter.BaseItem>()
    private val adapter = BaseAdapter(dataSource)
    private var itemCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.adapter = adapter

        insertNormal.setOnClickListener {
            dataSource.addGroup(10, MenuItem("item $itemCount") {})
            itemCount++
            adapter.notifyDataSetChanged()
        }
        insertHeader.setOnClickListener {
            val insertIndex = dataSource.addGroup(0, GroupTitleItem(0, " header") {
                dataSource.clearGroup(0)
                adapter.notifyDataSetChanged()
            })
            adapter.notifyItemInserted(insertIndex)
        }
        insertFooter.setOnClickListener {
            val insertIndex = dataSource.addGroup(100, GroupTitleItem(100, "footer") {
                dataSource.clearGroup(100)
                adapter.notifyDataSetChanged()
            })
            adapter.notifyItemInserted(insertIndex)
        }
    }
}