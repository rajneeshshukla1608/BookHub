package com.rajneesh.bookhub.util

import android.content.Context
import android.net.NetworkInfo
import android.net.ConnectivityManager

class ConnectionManger {
    fun checkConnectivity(context: Context) : Boolean{

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo

        if(activeNetwork != null){
            return activeNetwork.isConnected
        } else{

            return false

        }
    }
}