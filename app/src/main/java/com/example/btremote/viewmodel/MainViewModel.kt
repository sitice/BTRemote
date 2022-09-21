package com.example.btremote.viewmodel

import android.app.DownloadManager
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btremote.app.App
import com.example.btremote.compose.Screens
import com.example.btremote.database.protocol.Protocol
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*


class MainViewModel : ViewModel() {


    val nowScreenLiveData = MutableLiveData(Screens.mainScreen)

    val protocols = App.protocolDao.getAllProtocolFlow()

     val isPauseFlow = MutableStateFlow(false)

    fun pause(){
        isPauseFlow.value = true
    }
    fun playArrow(){
        isPauseFlow.value = false
    }

}