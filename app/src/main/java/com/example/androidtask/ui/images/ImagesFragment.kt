package com.example.androidtask.ui.images

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import com.example.androidtask.core.BaseFragmentWithViewModel
import com.example.androidtask.databinding.FragmentImagesBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "ImagesFragment"
@AndroidEntryPoint
class ImagesFragment : BaseFragmentWithViewModel<FragmentImagesBinding,ImagesViewModel>() {
    val imagesAdapter = ImagesAdapter()
    private val storagePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Log.d(TAG, "Permission Granted")
            } else {
                Log.d(TAG, "Permission Denied")
            }
        }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        setupImagesAdapter()
        getAllDeviceImages()
    }

    private fun setupImagesAdapter() {
        binding.imgsList.apply {
            adapter = imagesAdapter
            layoutManager = GridLayoutManager(context,3)

        }

    }

    private fun getAllDeviceImages() {
        viewModel.loadAllImages()
        observeImagesLiveData()
    }

    private fun observeImagesLiveData() {
        viewModel.imagesLiveData.observe(viewLifecycleOwner){

            imagesAdapter.fill(it)

        }
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            storagePermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)

        } else {
            storagePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

        }
    }


}