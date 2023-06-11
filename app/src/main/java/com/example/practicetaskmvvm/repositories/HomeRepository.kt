package com.example.practicetaskmvvm.repositories

import com.example.practicetaskmvvm.api.RetrofitInstance
import com.example.practicetaskmvvm.utils.CONSTANTS
import javax.inject.Inject

class HomeRepository @Inject constructor() {
    suspend fun getProductList() = RetrofitInstance.api.getProductList(CONSTANTS.PRODUCT_LIST_URL)
}