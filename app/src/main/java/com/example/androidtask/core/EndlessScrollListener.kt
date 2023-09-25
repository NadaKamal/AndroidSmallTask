package com.example.androidtask.core

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class EndlessScrollListener(private val layoutManager: RecyclerView.LayoutManager) :
    RecyclerView.OnScrollListener() {

    private var visibleThreshold = 1
    private var page = 0
    private var hasMore = true
    private var hasError = false
    private var currentlyLoading = false
    private var loadMoreListener: ((page: Int) -> Unit)? = null

    init {
        when (layoutManager) {
            is GridLayoutManager -> visibleThreshold *= layoutManager.spanCount
            is StaggeredGridLayoutManager -> visibleThreshold *= layoutManager.spanCount
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)

        if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
            var lastVisibleItemPosition = 0
            val totalItemCount = layoutManager.itemCount

            when (layoutManager) {
                is StaggeredGridLayoutManager ->
                    lastVisibleItemPosition =
                        getLastVisibleItem(layoutManager.findLastVisibleItemPositions(null))

                is GridLayoutManager -> lastVisibleItemPosition =
                    layoutManager.findLastVisibleItemPosition()

                is LinearLayoutManager -> lastVisibleItemPosition =
                    layoutManager.findLastVisibleItemPosition()
            }

            if (hasMore && totalItemCount <= lastVisibleItemPosition + visibleThreshold) {
                if (!currentlyLoading && !hasError) loadMoreListener?.invoke(++page)
            }
        }
    }

    fun getPage() = page

    fun reducePage() {
        if (page > 0) --page
    }

    fun resetPage() {
        page = 0
        hasMore = true
    }

    fun setLoaded() {
        hasMore = false
    }

    fun setCurrentlyLoading(currentlyLoading: Boolean) {
        this.currentlyLoading = currentlyLoading
    }

    fun setError(hasError: Boolean) {
        this.hasError = hasError
    }

    fun setOnLoadMoreListener(loadMoreListener: (page: Int) -> Unit): EndlessScrollListener {
        this.loadMoreListener = loadMoreListener
        return this
    }

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) maxSize = lastVisibleItemPositions[i]
            else if (lastVisibleItemPositions[i] > maxSize) maxSize = lastVisibleItemPositions[i]
        }
        return maxSize
    }
}