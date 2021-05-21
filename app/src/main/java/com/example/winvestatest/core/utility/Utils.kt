package com.example.androidtest.core

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo


@Suppress("DEPRECATION")
class Utils {
    companion object {
        fun isNetworkAvailable(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            return activeNetwork?.isConnectedOrConnecting == true
        }
    }
}