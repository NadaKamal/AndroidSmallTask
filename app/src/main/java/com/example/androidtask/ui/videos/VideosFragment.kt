package com.example.androidtask.ui.videos

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.androidtask.core.BaseFragmentWithViewModel
import com.example.androidtask.databinding.FragmentVideosBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "VideosFragment"

@AndroidEntryPoint
class VideosFragment : BaseFragmentWithViewModel<FragmentVideosBinding, VideosViewModel>() {

    private val videosAdapter = VideosAdapter()
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
        setupVideosAdapter()
        getAllVideos()

    }

    private fun getAllVideos() {
        viewModel.loadAllVideos()
        observeVideosLiveData()
    }

    private fun observeVideosLiveData() {
        viewModel.videosLiveData.observe(viewLifecycleOwner){
            videosAdapter.fill(it)
        }
    }

    private fun setupVideosAdapter() {
        binding.videosList.apply {
            adapter = videosAdapter
            layoutManager = GridLayoutManager(context,3)
        }
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            storagePermissionLauncher.launch(Manifest.permission.READ_MEDIA_VIDEO)

        } else {
            storagePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

        }
    }


}