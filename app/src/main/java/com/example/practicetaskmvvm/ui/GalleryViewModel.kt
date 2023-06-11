package com.example.practicetaskmvvm.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_ETHERNET
import android.net.ConnectivityManager.TYPE_MOBILE
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicetaskmvvm.PracticeApp
import com.example.practicetaskmvvm.R
import com.example.practicetaskmvvm.models.GalleryImages
import com.example.practicetaskmvvm.models.Product
import com.example.practicetaskmvvm.repositories.GalleryRepository
import com.example.practicetaskmvvm.repositories.HomeRepository
import com.example.practicetaskmvvm.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor (
    private val galleryRepository: GalleryRepository, app: Application
) : AndroidViewModel(app) {

    private val _imagesList = MutableStateFlow<NetworkResponse<GalleryImages>>(NetworkResponse.Loading())
    val imagesList = _imagesList.asStateFlow()

    init {
        getGalleryImages()
    }

    fun getGalleryImages() = viewModelScope.launch {
        try {
            if(hasInternetConnection()) {
                val response = galleryRepository.getGalleryImages()
                _imagesList.value = handleImagesListApiResponse(response)
            } else {
                _imagesList.value = NetworkResponse.Error("No Internet Connection")
            }
        } catch (t : Throwable) {
            _imagesList.value = NetworkResponse.Error(t.message.toString())
        }
    }

    private fun handleImagesListApiResponse(response: Response<GalleryImages>): NetworkResponse<GalleryImages> {
        if (response.isSuccessful) {
            response.body()?.let {
                return NetworkResponse.Success(it)
            }
        }
        return NetworkResponse.Error(response.message())
    }

    fun hasInternetConnection() : Boolean {
        val connectivityManager = getApplication<PracticeApp>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when{
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo.run {
                return when(this?.type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}