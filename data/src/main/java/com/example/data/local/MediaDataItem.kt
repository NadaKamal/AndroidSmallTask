package com.example.data.local

import android.net.Uri

data class MediaDataItem(
    val uri: Uri?=null,
    val name: String?=null,
    val duration: Int?=null,
    val size: Int?=null
)