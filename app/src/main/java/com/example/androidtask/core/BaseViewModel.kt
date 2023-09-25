package com.example.androidtask.core

import android.view.View
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.network.ApiFailure
import com.example.androidtask.loading.LoadingStatus


abstract class BaseViewModel : ViewModel() {

    private val loadingStatus by lazy { MutableLiveData<LoadingStatus>() }
    private val errorMessage by lazy { MutableLiveData<String>() }
    private val errorMessageRes by lazy { MutableLiveData<Int>() }
    private val emptyMessage by lazy { MutableLiveData<Int>() }
    private val showView by lazy { MutableLiveData<Boolean>() }

    abstract fun isFullScreenPage(): Boolean


    open fun onRetryClicked(@NonNull view: View) {}

    fun loading() {
        if (isFullScreenPage()) showView.postValue(false)
        loadingStatus.postValue(LoadingStatus.Loading)
    }

    fun success() {
        loadingStatus.postValue(LoadingStatus.Success)
        showView.postValue(true)
    }

    fun error(apiFailure: ApiFailure) {
        if (apiFailure.error.isNullOrEmpty())
            errorMessageRes.value = apiFailure.errorResId
        else
            errorMessage.value = apiFailure.error

        apiFailure.error?.let(errorMessage::postValue)
        apiFailure.errorResId?.let(errorMessageRes::postValue)

        if (isFullScreenPage()) {
            showView.postValue(false)
            loadingStatus.postValue(LoadingStatus.Retry)
        } else
            loadingStatus.postValue(LoadingStatus.Error)

        clearError()
    }

    fun noContent(@StringRes message: Int) {
        loadingStatus.postValue(LoadingStatus.NoContent)
        emptyMessage.postValue(message)
        showView.postValue(false)
    }

    fun stopLoading() = loadingStatus.postValue(LoadingStatus.Initial)

    private fun clearError() {
        errorMessage.postValue(null)
        errorMessageRes.postValue(null)
    }
}