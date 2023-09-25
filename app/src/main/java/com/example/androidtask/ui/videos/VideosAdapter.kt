package com.example.androidtask.ui.videos


import com.example.androidtask.core.BaseAdapter
import com.example.androidtask.core.ContextHolder
import com.example.androidtask.databinding.VideoItemBinding
import com.example.data.extentions.extractImageFromVideoUri
import com.example.data.local.MediaDataItem

class VideosAdapter:BaseAdapter<VideoItemBinding, MediaDataItem>(){
    override fun setContent(binding: VideoItemBinding, item: MediaDataItem, position: Int) {
        item.uri?.extractImageFromVideoUri(ContextHolder.getContext(),binding.videoImg)

    }
}