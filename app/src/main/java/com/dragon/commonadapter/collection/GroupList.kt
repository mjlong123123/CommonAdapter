package com.dragon.commonadapter.collection

/**
 * @author dragon
 */
class GroupList<T> : MutableList<T> {
    private val dataList = mutableListOf<Data<T>>()
    override val size: Int
        get() = dataList.size

    fun addGroupData(group: Int, element: T): Int {
        var insertIndex = -1
        for (index in 0 until dataList.size) {
            if (dataList[index].group > group) {
                insertIndex = index
                break
            }
        }

        return if (insertIndex >= 0) {
            dataList.add(insertIndex, Data(group, element))
            insertIndex
        } else {
            dataList.add(Data(group, element))
            dataList.size - 1
        }
    }

    fun addGroupAllData(group: Int, elements: Collection<T>): Int {
        var insertIndex = -1
        for (index in 0 until dataList.size) {
            if (dataList[index].group > group) {
                insertIndex = index
                break
            }
        }
        return if (insertIndex >= 0) {
            dataList.addAll(insertIndex, elements.map { Data(group, it) })
            insertIndex
        } else {
            dataList.addAll(elements.map { Data(group, it) })
            dataList.size - 1
        }
    }

    fun clearGroup(group: Int) {
        dataList.listIterator().let {
            while (it.hasNext()) {
                if (it.next().group == group) {
                    it.remove()
                }
            }
        }
    }

    override fun contains(element: T): Boolean {
        throw UnsupportedOperationException("GroupList not support contains")
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        throw UnsupportedOperationException("GroupList not support containsAll")
    }

    override fun get(index: Int) = dataList[index].data

    override fun indexOf(element: T): Int {
        dataList.forEachIndexed { index, data ->
            if (data.data == element) return index
        }
        return -1
    }

    override fun isEmpty() = dataList.isEmpty()

    override fun iterator(): MutableIterator<T> {
        return object : MutableIterator<T> {
            val internalIterator = dataList.iterator()
            override fun hasNext() = internalIterator.hasNext()

            override fun next() = internalIterator.next().data

            override fun remove() {
                internalIterator.remove()
            }
        }
    }

    override fun lastIndexOf(element: T): Int {
        for (index in (dataList.size - 1) downTo 0) {
            if (dataList[index].data == element)
                return index
        }
        return -1
    }

    override fun add(element: T) =
        dataList.add(Data(if (isEmpty()) 0 else dataList[dataList.size - 1].group, element))

    override fun add(index: Int, element: T) {
        if (isEmpty()) {
            dataList.add(Data(0, element))
            return
        }
        dataList.add(index, Data(dataList[if (index == 0) 0 else index - 1].group, element))
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        if (isEmpty()) {
            return dataList.addAll(elements.map { Data(0, it) })
        }
        val group = dataList[if (index == 0) 0 else index - 1].group
        return dataList.addAll(index, elements.map { Data(group, it) })
    }

    override fun addAll(elements: Collection<T>): Boolean {
        if (isEmpty()) {
            return dataList.addAll(elements.map { Data(0, it) })
        }
        val group = dataList[dataList.size - 1].group
        return dataList.addAll(elements.map { Data(group, it) })
    }

    override fun clear() {
        dataList.clear()
    }

    override fun listIterator(): MutableListIterator<T> {
        throw UnsupportedOperationException("GroupList not support listIterator")
    }

    override fun listIterator(index: Int): MutableListIterator<T> {
        throw UnsupportedOperationException("GroupList not support listIterator")
    }

    override fun remove(element: T): Boolean {
        var ret = false
        dataList.listIterator().let {
            while (it.hasNext()) {
                if (it.next().data == element) {
                    it.remove()
                    ret = true
                    break
                }
            }
        }
        return ret
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        throw UnsupportedOperationException("GroupList not support removeAll")
    }

    override fun removeAt(index: Int): T = dataList.removeAt(index).data

    override fun retainAll(elements: Collection<T>): Boolean {
        throw UnsupportedOperationException("GroupList not support retainAll")
    }

    override fun set(index: Int, element: T): T {
        throw UnsupportedOperationException("GroupList not support set")
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> {
        throw UnsupportedOperationException("GroupList not support set")
    }

    override fun toString(): String {
        val sb = StringBuilder("[")
        dataList.forEach { sb.append(it.toString()).append(",") }
        sb.replace(sb.length - 1, sb.length, "]")
        return sb.toString()
    }

    fun toDataString(): String {
        val sb = StringBuilder("[")
        dataList.forEach { sb.append(it.data).append(",") }
        sb.replace(sb.length - 1, sb.length, "]").append(" size: $size")
        return sb.toString()
    }

    fun toGroupString(): String {
        val sb = StringBuilder("[")
        dataList.forEach { sb.append(it.group).append(",") }
        sb.replace(sb.length - 1, sb.length, "]").append(" size: $size")
        return sb.toString()
    }

    data class Data<T>(val group: Int, val data: T)
}