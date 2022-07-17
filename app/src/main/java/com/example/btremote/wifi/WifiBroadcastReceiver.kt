package com.example.btremote.wifi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Build
import androidx.annotation.RequiresApi

class WifiBroadcastReceiver(private val wifiManager: WifiManager): BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(p0: Context?, p1: Intent?) {
        val success = p1?.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED,false)
        if (success == true){
            scanSuccess()
        }else{
            scanFailure()
        }
    }

    private fun scanSuccess(){
        val result = wifiManager.scanResults
    }

    private fun scanFailure(){
        val result = wifiManager.scanResults
    }

}