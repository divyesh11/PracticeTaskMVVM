package com.example.practicetaskmvvm.repositories

import com.example.practicetaskmvvm.api.RetrofitInstance
import com.example.practicetaskmvvm.utils.CONSTANTS
import javax.inject.Inject

class GalleryRepository @Inject constructor() {
    suspend fun getGalleryImages() = RetrofitInstance.api.getGalleryImages(CONSTANTS.GALLERY_IMAGES_URL)
}