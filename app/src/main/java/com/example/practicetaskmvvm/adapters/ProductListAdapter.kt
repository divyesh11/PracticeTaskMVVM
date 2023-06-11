package com.example.practicetaskmvvm.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.practicetaskmvvm.R
import com.example.practicetaskmvvm.models.ProductX
import com.squareup.picasso.Picasso

class ProductListAdapter(
    val context : Context
) : RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<ProductX>() {
        override fun areItemsTheSame(oldItem: ProductX, newItem: ProductX): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductX, newItem: ProductX): Boolean {
            return oldItem == newItem
        }
    }

    val mDiffer = AsyncListDiffer(this,differCallback)

    inner class ProductViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var parentLayout : LinearLayout
        var productImage : ImageView
        var productTitle : TextView
        var productSubtitle : TextView

        init {
            parentLayout = itemView.findViewById(R.id.parent_layout)
            productImage = itemView.findViewById(R.id.product_image)
            productTitle = itemView.findViewById(R.id.product_title)
            productSubtitle = itemView.findViewById(R.id.product_subtitle)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(LayoutInflater.from(parent.context).inflate(
                R.layout.item_product,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mDiffer.currentList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = mDiffer.currentList[position]
        holder.apply {
            Picasso.get().load(product.thumbnail).placeholder(R.drawable.placeholder_homescreen_item).into(productImage)
            productTitle.text = product.title
            productSubtitle.text = product.description
        }
        holder.parentLayout.apply {
            setOnClickListener { v->
                Log.d("Adapter","reached")
                onItemClickListener?.let {
                    it(product)
                }
            }
        }
    }

    private var onItemClickListener: ((ProductX) -> Unit)? = null

    fun setOnItemClickListener(listener : (ProductX) -> Unit) {
        onItemClickListener = listener
    }
}