package com.example.androidtask.ui.images

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.androidtask.core.BaseViewModel
import com.example.data.local.MediaDataItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ImagesViewModel"

@HiltViewModel
class ImagesViewModel @Inject constructor() : BaseViewModel() {

    override fun isFullScreenPage() = false

    private val _imagesLiveData = MutableLiveData<MutableList<MediaDataItem>>()
    val imagesLiveData get() = _imagesLiveData



    fun loadAllImages() {
        loading()
        viewModelScope.launch {
            _imagesLiveData.value = getAllImages()
            success()

        }
    }




}





