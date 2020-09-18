package com.dragon.commonadapter.list

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dragon.commonadapter.list.datasource.PagingDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author dragon
 */
class PageLoader<T : PageLoader.PreviousNextContainer>(
    private val scope: CoroutineScope,
    private val dataSource: PagingDataSource<BaseAdapter.BaseItem, T>,
    private val preOffset: Int = 1,
    private val initKey: String = "",
    private val loadAction: suspend (Parameter) -> T
) : RecyclerView.OnScrollListener(), CoroutineScope by scope {
    private var isLoading = false
    private var previousKey: String? = ""
    private var nextKey: String? = ""
    private var internalPageResult = MutableLiveData<PageResult<T>>()

    val pageResult = MediatorLiveData<PageResult<T>>()

    init {
        pageResult.observeForever {
            dataSource.updateHeaderOrFooter(it)
        }
        loadFirst()
    }


    fun attachRecycleView(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(this)
    }

    fun refresh() {
        previousKey = ""
        nextKey = ""
        loadFirst()
    }

    private fun loadFirst() {
        load(Parameter.INIT(initKey))
    }

    private fun loadNext() {
        load(Parameter.NEXT(nextKey ?: ""))
    }

    private fun loadPrevious() {
        load(Parameter.PREVIOUS(previousKey ?: ""))
    }

    private fun load(p: Parameter) {
        isLoading = true
        pageResult.removeSource(internalPageResult)
        internalPageResult = MutableLiveData()
        pageResult.addSource(internalPageResult) {
            pageResult.value = it
        }
        internalPageResult.postValue(PageResult.Loading(p))
        launch(Dispatchers.IO) {
            val ret = runCatching {
                loadAction.invoke(p)
            }
            if (ret.isSuccess) {
                ret.getOrNull()?.let { data ->
                    internalPageResult.postValue(PageResult.Success(p, data))
                    when (p) {
                        is Parameter.INIT -> {
                            (data as? PreviousNextContainer)?.let { it ->
                                previousKey = it.previousKey()
                                nextKey = it.nextKey()
                            }
                        }
                        is Parameter.NEXT -> {
                            (data as? PreviousNextContainer)?.let { it ->
                                nextKey = it.nextKey()
                            }
                        }
                        is Parameter.PREVIOUS -> {
                            (data as? PreviousNextContainer)?.let { it ->
                                previousKey = it.previousKey()
                            }
                        }
                    }
                }
            } else {
                internalPageResult.postValue(
                    PageResult.Failed(
                        p,
                        ret.exceptionOrNull()
                    ) { load(p) })
            }
            isLoading = false
        }
    }


    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (!isLoading && newState == RecyclerView.SCROLL_STATE_IDLE) {
            if (!nextKey.isNullOrEmpty()) {
                recyclerView.adapter?.let {
                    val lastPosition = (recyclerView.layoutManager as? LinearLayoutManager)?.findLastVisibleItemPosition() ?: RecyclerView.NO_POSITION
                    if (lastPosition >= it.itemCount - preOffset) {
                        loadNext()
                        return
                    }
                }
            }
            if (!previousKey.isNullOrEmpty()) {
                val firstPosition = (recyclerView.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition() ?: RecyclerView.NO_POSITION
                if (firstPosition in 0 until preOffset) {
                    loadPrevious()
                }
            }
        }
    }

    sealed class Parameter(val key: String) {
        class INIT(key: String) : Parameter(key)
        class PREVIOUS(key: String) : Parameter(key)
        class NEXT(key: String) : Parameter(key)
    }

    sealed class PageResult<T>(val parameter: Parameter) {
        class Loading<T>(parameter: Parameter) : PageResult<T>(parameter)
        class Success<T>(parameter: Parameter, val data: T) : PageResult<T>(parameter)
        class Failed<T>(parameter: Parameter, val error: Throwable?, val retry: (() -> Unit)? = null) : PageResult<T>(parameter)
    }

    interface PreviousNextContainer {
        fun previousKey(): String?
        fun nextKey(): String?
    }
}