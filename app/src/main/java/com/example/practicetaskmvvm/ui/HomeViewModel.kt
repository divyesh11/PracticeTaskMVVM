package com.example.practicetaskmvvm.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicetaskmvvm.PracticeApp
import com.example.practicetaskmvvm.models.Product
import com.example.practicetaskmvvm.models.ProductX
import com.example.practicetaskmvvm.repositories.HomeRepository
import com.example.practicetaskmvvm.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor (
    private val homeRepository : HomeRepository,
    app: Application
) : AndroidViewModel(app) {

    private val _productList = MutableStateFlow<NetworkResponse<Product>>(NetworkResponse.Loading())
    val productList = _productList.asStateFlow()

    init {
        getProductList()
    }

    fun getProductList() = viewModelScope.launch {
        try {
            if(hasInternetConnection()) {
                val response = homeRepository.getProductList()
                _productList.value = handleProductListApiResponse(response)
            } else {
                _productList.value = NetworkResponse.Error("No Internet Connection")
            }
        } catch (t : Throwable) {
            _productList.value = NetworkResponse.Error(t.message.toString())
        }
    }

    private fun handleProductListApiResponse(response: Response<Product>) : NetworkResponse<Product> {
        if(response.isSuccessful) {
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
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo.run {
                return when(this?.type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}