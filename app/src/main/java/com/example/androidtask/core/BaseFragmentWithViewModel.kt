package com.example.androidtask.core

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

abstract class BaseFragmentWithViewModel<VB : ViewBinding, VM : ViewModel> : BaseFragment<VB>() {

    protected lateinit var viewModel: VM
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = initViewModel()
    }

    @Suppress("UNCHECKED_CAST")
    private fun initViewModel(): VM {
        val parameterizedType = javaClass.genericSuperclass as ParameterizedType
        val vmClass = parameterizedType.actualTypeArguments[1] as Class<VM>
        return ViewModelProvider(this)[vmClass]
    }
}