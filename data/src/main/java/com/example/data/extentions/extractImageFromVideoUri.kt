package com.example.data.extentions

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.widget.ImageView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
fun Uri.extractImageFromVideoUri(context: Context, imageView: ImageView) {
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(context, this)
    GlobalScope.launch(Dispatchers.Main) {
        val bitmap = retriever.frameAtTime
        imageView.setImageBitmap(bitmap)
        retriever.release()
    }
}