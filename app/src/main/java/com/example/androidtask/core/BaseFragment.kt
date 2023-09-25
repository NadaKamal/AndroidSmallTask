package com.example.androidtask.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType


abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = initBinding()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @Suppress("UNCHECKED_CAST")
    private fun initBinding(): VB {
        val parameterizedType = javaClass.genericSuperclass as ParameterizedType
        val vbClass = parameterizedType.actualTypeArguments[0] as Class<VB>
        val inflate = vbClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        return inflate.invoke(null, layoutInflater) as VB
    }

}