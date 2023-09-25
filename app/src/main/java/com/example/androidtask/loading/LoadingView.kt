package com.example.androidtask.loading

import android.app.AlertDialog
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import com.example.androidtask.R
import com.example.androidtask.databinding.ViewLoadingBinding
import com.example.data.extentions.gone
import com.example.data.extentions.visible


private const val TAG = "LoadingView"

class LoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RelativeLayout(context, attrs) {

    private var alertDialog: AlertDialog? = null
    private var binding: ViewLoadingBinding

    var errorResId = 0
    var errorText = ""

    var loadingText = 0
    var emptyText = 0

    var onRetryClicked: OnClickListener? = null
        set(value) = binding.btnRetry.setOnClickListener(value)

    init {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.view_loading, this, true
        )
    }

    fun loadingStatusChanged(loadingStatus: LoadingStatus) {
        when (loadingStatus) {
            LoadingStatus.Loading -> load()
            LoadingStatus.Retry -> showRetry()
            LoadingStatus.Error -> showError()
            LoadingStatus.NoContent -> showEmptyView()
            else -> gone()
        }
    }

    private fun load() = with(binding) {

        visible()
        loadingView.visible()
        llError.gone()
        llEmpty.gone()

    }

    fun showLoadingMessage() = with(binding) {
//        if (loadingText != 0) loadingView.setTextMsg(context.getString(loadingText))
    }

    private fun showRetry() = with(binding) {
//    if (errorResId == R.string.network_unauthorized)
//    {
//
//    }
//    else {
        visible()
        loadingView.gone()
        llError.visible()
        llEmpty.gone()
        showRetryMessage()
//    }
    }


    private fun showRetryMessage() = with(binding) {
        btnRetry.visible()
        showErrorMessage()
    }

    fun showError() = with(binding) {
        visible()
        loadingView.gone()
        llError.visible()
        llEmpty.gone()
        btnRetry.gone()
        showErrorMessage()

    }

    fun showErrorMessage() = with(binding) {
        if (errorText.isNotEmpty()) tvError.text = errorText
        if (errorResId != 0) tvError.setText(errorResId)
    }

    private fun showEmptyView() = with(binding) {
        visible()
        loadingView.gone()
        llError.gone()
        llEmpty.visible()
        showEmptyMessage()
    }

    fun showEmptyMessage() {
        if (emptyText != 0) binding.tvEmpty.setText(emptyText)
    }
}

@BindingAdapter("app:loadingStatus")
fun LoadingView.setLoadingStatus(loading: LoadingStatus?) {
    loading?.let { loadingStatusChanged(it) }
}

@BindingAdapter("app:loadingText")
fun LoadingView.setLoadingText(res: Int?) {
    res?.let {
        loadingText = res
        showLoadingMessage()
    }
}

@BindingAdapter("app:errorResId")
fun LoadingView.setErrorResId(res: Int?) {
    res?.let {
        errorResId = res
        showErrorMessage()
    }
}

@BindingAdapter("app:errorText")
fun LoadingView.setErrorText(error: String?) {
    error?.let {
        errorText = error
        showError()
    }
}

@BindingAdapter("app:emptyText")
fun LoadingView.setEmptyText(res: Int?) {
    res?.let {
        emptyText = res
        showEmptyMessage()
    }
}

enum class LoadingStatus {
    Initial, Loading, Error, Retry, Success, NoContent
}