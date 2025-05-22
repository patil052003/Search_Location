package com.vaishnavi.tryretro.api

//T for     w eatherModel
sealed class NetworkResponse<out T> {
    data class Success<out T>(val data: T) : NetworkResponse<T>()
    data class Error(val message: String, val throwable: Throwable? = null) : NetworkResponse<Nothing>()
    object Loading : NetworkResponse<Nothing>()

}
