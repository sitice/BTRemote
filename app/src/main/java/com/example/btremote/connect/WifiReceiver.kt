package com.example.btremote.connect

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.btremote.app.App

class WifiReceiver:BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onReceive(p0: Context?, p1: Intent?) {
        when (p1?.action) {
            WifiManager.WIFI_STATE_CHANGED_ACTION -> {
                when (p1.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0)) {
                    WifiManager.WIFI_STATE_DISABLED -> {
                        App.wifiState = WifiStatus.DISABLE
                    }
                    WifiManager.WIFI_STATE_DISABLING -> {
                        App.wifiState = WifiStatus.TURNING_OFF
                    }
                    WifiManager.WIFI_STATE_ENABLED -> {
                        App.wifiService.getNetStatus()
                    }
                    WifiManager.WIFI_STATE_ENABLING -> {
                        App.wifiState = WifiStatus.TURNING_ON
                    }
                }
            }
        }
    }
}