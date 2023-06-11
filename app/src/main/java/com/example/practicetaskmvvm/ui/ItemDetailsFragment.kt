package com.example.practicetaskmvvm.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.practicetaskmvvm.databinding.FragmentItemDetailsBinding
import com.example.practicetaskmvvm.models.ProductX
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemDetailsFragment : Fragment() {
    private lateinit var viewBinding : FragmentItemDetailsBinding
    val args : ItemDetailsFragmentArgs by navArgs()

    private lateinit var mProduct : ProductX

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentItemDetailsBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mProduct = args.product
        updateUI()
    }

    private fun updateUI() {
        Picasso.get().load(mProduct.thumbnail).into(viewBinding.productImage)
        viewBinding.productTitle.text = mProduct.title
        viewBinding.productPrice.text = mProduct.price.toString()
        viewBinding.productDiscount.text = mProduct.discountPercentage.toString()
    }
}