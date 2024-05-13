package com.example.landmarkremark.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object Utils {
	fun isNetworkAvailable(context: Context): Boolean {
		(context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
			return getNetworkCapabilities(activeNetwork)?.run {
				when {
					hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
					hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
					hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
					else -> false
				}
			} ?: false
		}
	}
}