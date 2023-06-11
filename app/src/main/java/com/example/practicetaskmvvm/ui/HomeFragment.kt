package com.example.practicetaskmvvm.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView.RecyclerListener
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practicetaskmvvm.R
import com.example.practicetaskmvvm.adapters.ProductListAdapter
import com.example.practicetaskmvvm.databinding.FragmentHomeBinding
import com.example.practicetaskmvvm.utils.NetworkResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val TAG = "HomeFragment"

    private lateinit var viewBinding : FragmentHomeBinding
    private lateinit var productListAdapter : ProductListAdapter

    private val viewModel : HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentHomeBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()

        productListAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("product",it)
            }
            findNavController().navigate(
                R.id.action_homeFragment_to_itemDetailsFragment,
                bundle
            )
        }
        collectProductListData()
    }

    private fun collectProductListData() {
        lifecycleScope.launch {
            viewModel.productList.collect {
                when(it) {
                    is NetworkResponse.Success -> {
                        Log.d(TAG,"Success")
                        productListAdapter.mDiffer.submitList(it.data?.products)
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
        productListAdapter = ProductListAdapter(this.requireContext())
        viewBinding.productList.apply {
            adapter = productListAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}