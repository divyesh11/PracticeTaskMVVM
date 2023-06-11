package com.example.practicetaskmvvm.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practicetaskmvvm.adapters.ImagesListAdapter
import com.example.practicetaskmvvm.adapters.ProductListAdapter
import com.example.practicetaskmvvm.databinding.FragmentGalleryBinding
import com.example.practicetaskmvvm.utils.NetworkResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GalleryFragment : Fragment() {

    private val TAG = "GalleryFragment"

    private lateinit var viewBinding : FragmentGalleryBinding

    private lateinit var imagesListAdapter : ImagesListAdapter

    private val viewModel : GalleryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentGalleryBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        collectImagesListData()
    }

    private fun collectImagesListData() {
        lifecycleScope.launch {
            viewModel.imagesList.collect {
                when(it) {
                    is NetworkResponse.Success -> {
                        Log.d(TAG,"Success")
                        imagesListAdapter.mDiffer.submitList(it.data)
                    }
                    is NetworkResponse.Error -> {
                        Log.d(TAG,"Error${it.message}")
                        showErrorToast(it.message.toString())
                    }
                    is NetworkResponse.Loading -> {
                        Log.d(TAG,"Loading")
                    }
                }
            }
        }
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(this.requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun setUpRecyclerView() {
        imagesListAdapter = ImagesListAdapter(this.requireContext())
        viewBinding.imagesList.apply {
            adapter = imagesListAdapter
            layoutManager = GridLayoutManager(activity,3)
        }
    }
}