package com.example.practicetaskmvvm.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.practicetaskmvvm.R
import com.example.practicetaskmvvm.models.GalleryImagesItem
import com.squareup.picasso.Picasso

class ImagesListAdapter(
    val context : Context
) : RecyclerView.Adapter<ImagesListAdapter.ImagesViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<GalleryImagesItem>() {
        override fun areItemsTheSame(oldItem: GalleryImagesItem, newItem: GalleryImagesItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GalleryImagesItem, newItem: GalleryImagesItem): Boolean {
            return oldItem == newItem
        }
    }

    val mDiffer = AsyncListDiffer(this,differCallback)

    inner class ImagesViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var image : ImageView

        init {
            image = itemView.findViewById(R.id.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        return ImagesViewHolder(LayoutInflater.from(parent.context).inflate(
                R.layout.item_gallery,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mDiffer.currentList.size
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        val product = mDiffer.currentList[position]
        holder.apply {
            Picasso.get().load(product.url).placeholder(R.drawable.placeholder_gallary_item).into(image)
        }
    }

    private var onItemClickListener: ((GalleryImagesItem) -> Unit)? = null

    fun setOnItemClickListener(listener : (GalleryImagesItem) -> Unit) {
        onItemClickListener = listener
    }
}