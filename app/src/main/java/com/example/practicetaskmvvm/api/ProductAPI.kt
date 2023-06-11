package com.example.practicetaskmvvm.api

import com.example.practicetaskmvvm.models.GalleryImages
import com.example.practicetaskmvvm.models.Product
import com.example.practicetaskmvvm.models.ProductX
import com.example.practicetaskmvvm.utils.NetworkResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ProductAPI {
    @GET
    suspend fun getProductList(@Url url : String) : Response<Product>

    @GET
    suspend fun getGalleryImages(@Url url : String) : Response<GalleryImages>
}