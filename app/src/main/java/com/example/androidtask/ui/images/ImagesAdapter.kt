package com.example.androidtask.ui.images

import com.example.androidtask.core.BaseAdapter
import com.example.androidtask.databinding.ImageItemBinding
import com.example.data.local.MediaDataItem

class ImagesAdapter:BaseAdapter<ImageItemBinding, MediaDataItem>(){
    override fun setContent(binding: ImageItemBinding, item: MediaDataItem, position: Int) {
        binding.img.setImageURI(item.uri)
    }
}