package com.example.androidtask.ui.images

import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.example.androidtask.core.ContextHolder
import com.example.data.local.MediaDataItem

private const val TAG = "getAllImages"


fun getAllImages(): MutableList<MediaDataItem> {

        val imagesList = mutableListOf<MediaDataItem>()
        val collection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL
                )
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DURATION,
            MediaStore.Images.Media.SIZE
        )

        val sortOrder = "${MediaStore.Images.Media.DISPLAY_NAME} ASC"

        val _query = ContextHolder.getContext().contentResolver.query(
            collection,
            projection,
            null,
            null,
            sortOrder
        )
        _query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val durationColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DURATION)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val duration = cursor.getInt(durationColumn)
                val size = cursor.getInt(sizeColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                imagesList += MediaDataItem(contentUri, name, duration, size)
                Log.d(TAG, "getAllImages: $imagesList")
            }
            return imagesList
        }

    return mutableListOf(MediaDataItem())

    }
