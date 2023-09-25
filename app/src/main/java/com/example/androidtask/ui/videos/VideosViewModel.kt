package com.example.androidtask.ui.videos

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.androidtask.core.BaseViewModel
import com.example.data.local.MediaDataItem
import kotlinx.coroutines.launch

class VideosViewModel : BaseViewModel() {
    override fun isFullScreenPage()= false

    private val _videosLiveData = MutableLiveData<MutableList<MediaDataItem>>()
    val videosLiveData get() = _videosLiveData

    fun loadAllVideos() {
        loading()
        viewModelScope.launch {
            _videosLiveData.value = getAllVideos()
            success()

        }
    }


}