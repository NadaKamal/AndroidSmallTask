package com.example.androidtask.core

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import com.example.androidtask.databinding.ItemLoadMoreBinding
import com.example.androidtask.databinding.ItemLoadMoreErrorBinding
import java.lang.reflect.ParameterizedType


private const val PRIMARY = 0
private const val LOADING = 1
private const val ERROR = 2

// TODO: Add header and footer. Make view type dynamic. Review getCurrentItems().
abstract class BaseAdapter<VB : ViewBinding, T> : Adapter<ViewHolder>(), Filterable {

    private var hasMore = true
    private var isError = false
    private var items = listOf<T>()
    private var mutableItems = items.toMutableList<T?>()
    private var filteredList = mutableListOf<T?>()
    private var clickListener: ((clickedView: View, item: T, position: Int) -> Unit)? = null
    private var loadMoreListener: ((page: Int) -> Unit)? = null
    private var scrollListener: EndlessScrollListener? = null
    private lateinit var parent: ViewGroup

    private val loadBinding by lazy {
        ItemLoadMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    private val errorBinding by lazy {
        ItemLoadMoreErrorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun getItemCount() = mutableItems.size

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        scrollListener = recyclerView.layoutManager?.let {
            EndlessScrollListener(it).setOnLoadMoreListener {
                if (hasMore) loadMoreListener?.let { listener ->
                    if (mutableItems.last() != null) {
                        showLoading()
                        listener.invoke(it)
                    }
                }
            }
        }
        scrollListener?.let { recyclerView.addOnScrollListener(it) }
    }

    override fun getItemViewType(position: Int) = if (hasMore) {
        when (mutableItems[position]) {
            null -> if (isError) ERROR else LOADING
            else -> PRIMARY
        }
    } else super.getItemViewType(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        this.parent = parent
        return when (viewType) {
            LOADING -> LoadingViewHolder(loadBinding)
            ERROR -> ErrorViewHolder(errorBinding)
            else -> MainViewHolder(getMainBinding())
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            LOADING -> (holder as BaseAdapter<VB, T>.LoadingViewHolder)
            ERROR -> (holder as BaseAdapter<VB, T>.ErrorViewHolder).setContent()
            PRIMARY -> (holder as BaseAdapter<VB, T>.MainViewHolder).setContent(mutableItems[position])
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun getFilter() = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val charString = constraint.toString()
            mutableItems = if (charString.isEmpty()) items.toMutableList() else filteredList

            val filterResults = FilterResults()
            filterResults.values = mutableItems
            return filterResults
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            mutableItems = results.values as MutableList<T?>
            notifyDataSetChanged()
        }
    }

    /**
     * Set on view click listener
     * @param clickListener (clickedView, clickedItem, clickedPosition)
     */
    fun setOnClickListener(clickListener: (clickedView: View, item: T, position: Int) -> Unit) {
        this.clickListener = clickListener
    }

    /**
     * Set on load more listener
     * @param loadMoreListener (page)
     */
    fun setOnLoadMoreListener(loadMoreListener: (page: Int) -> Unit) {
        this.loadMoreListener = loadMoreListener
        hasMore = true
    }

    /**
     * Set filtered data
     * @param filteredList
     */
    fun submitFilteredList(filteredList: MutableList<T?>) {
        this.filteredList = filteredList
    }

    /**
     * Get items
     */
    fun getCurrentItems() = mutableItems

    /**
     * Get item by position
     * @param position
     */
    fun getItem(position: Int) = mutableItems[position]!!

    /**
     * Replace current items with new items
     * @param items New items to fill
     */
    @SuppressLint("NotifyDataSetChanged")
    fun fill(items: List<T>) {
        this.items = items
        mutableItems.clear()
        mutableItems.addAll(items)
        notifyDataSetChanged()
    }

    /**
     * Add items to end of list
     * @param items New items to add
     */
    @SuppressLint("NotifyDataSetChanged")
    fun addItems(items: List<T>) {
        if (hasMore) dismissLoading()
        mutableItems.addAll(items)
        notifyDataSetChanged()
    }

    /**
     * Add item to end of list
     * @param item New item to add
     */
    @SuppressLint("NotifyDataSetChanged")
    fun addItem(item: T) {
        mutableItems.add(item)
        notifyDataSetChanged()
    }

    /**
     * Add item to certain position in list
     * @param position Where to add item
     * @param item New item to add
     */
    @SuppressLint("NotifyDataSetChanged")
    fun addItem(position: Int, item: T) {
        mutableItems.add(position, item)
        notifyDataSetChanged()
    }

    /**
     * Replace item in certain position
     * @param position Where to add item
     * @param item New item to replace with
     */
    @SuppressLint("NotifyDataSetChanged")
    fun replace(position: Int, item: T) {
        mutableItems[position] = item
        notifyDataSetChanged()
    }

    /**
     * Remove item from list
     * @param position Item position
     * @return List size
     */
    @SuppressLint("NotifyDataSetChanged")
    fun removeItem(position: Int): Int {
        mutableItems.removeAt(position)
        notifyDataSetChanged()
        return mutableItems.size
    }

    /**
     * Remove all items from list
     */
    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        items = listOf()
        mutableItems.clear()
        notifyDataSetChanged()
    }

    /**
     * Show loading
     */
    @SuppressLint("NotifyDataSetChanged")
    fun showLoading() {
        setCurrentlyLoading(true)
        mutableItems.add(null)
        notifyDataSetChanged()
    }

    /**
     * Dismiss loading
     */
    @SuppressLint("NotifyDataSetChanged")
    fun dismissLoading() {
        if (mutableItems.isNotEmpty() && mutableItems.last() == null) {
            setCurrentlyLoading(false)
            mutableItems.removeAt(mutableItems.lastIndex)
            notifyDataSetChanged()
        }
    }

    /**
     * Notify that no more items
     */
    fun setLoaded() {
        hasMore = false
        dismissLoading()
        scrollListener?.setLoaded()
    }

    /**
     * Show error
     */
    @SuppressLint("NotifyDataSetChanged")
    fun showError() {
        dismissLoading()
        setError(true)
        isError = true
        mutableItems.add(null)
        notifyDataSetChanged()
    }

    /**
     * Dismiss error
     */
    @SuppressLint("NotifyDataSetChanged")
    fun dismissError() {
        if (mutableItems.isNotEmpty()) {
            hasMore = true
            isError = false
            setError(false)
            mutableItems.removeAt(mutableItems.lastIndex)
            notifyDataSetChanged()
        }
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    /**
     * Get current page
     */
    fun getPage() = scrollListener?.getPage() ?: 0

    private fun setError(isError: Boolean) = scrollListener?.setError(isError)

    private fun setCurrentlyLoading(currentlyLoading: Boolean) =
        scrollListener?.setCurrentlyLoading(currentlyLoading)

    /**
     * Reset page to 0
     */
    fun resetPage() {
        hasMore = true
        scrollListener?.resetPage()
    }

    /**
     * Set view content
     * @param binding Bind view
     * @param item Item object
     * @param position Item position
     */
    protected abstract fun setContent(binding: VB, item: T, position: Int)

    /**
     * On view clicked
     * @param view Clicked view
     * @param item Clicked item
     * @param position Clicked position
     */
    protected fun onViewClicked(view: View, item: T, position: Int) {
        clickListener?.invoke(view, item, position)
    }

    @Suppress("UNCHECKED_CAST")
    private fun getMainBinding(): VB {
        val superclass = javaClass.genericSuperclass as ParameterizedType
        val method = (superclass.actualTypeArguments[0] as Class<Any>).getDeclaredMethod(
            "inflate",
            LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java
        )
        return method.invoke(null, LayoutInflater.from(parent.context), parent, false) as VB
    }

    inner class MainViewHolder(private val binding: VB) : ViewHolder(binding.root) {

        fun setContent(item: T?) {
            if (item != null) setContent(binding, item, adapterPosition)
        }
    }


    inner class LoadingViewHolder(binding: ItemLoadMoreBinding) : ViewHolder(binding.root)

    inner class ErrorViewHolder(private val binding: ItemLoadMoreErrorBinding) :
        ViewHolder(binding.root) {

        fun setContent() {
            if (isError) binding.llError.setOnClickListener {
                dismissError()
                showLoading()
                loadMoreListener?.invoke(scrollListener!!.getPage())
            }
        }
    }
}