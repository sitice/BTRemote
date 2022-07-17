package com.example.btremote.wifi

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Parcel
import android.os.Parcelable

class WifiService(context: Context)  {
    private val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    init {
        if (wifiManager.isWifiEnabled){
            wifiManager.startScan()
        }
    }
}