package com.example.btremote.viewmodel

import android.app.Instrumentation
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.animation.core.animateDpAsState
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btremote.app.App
import com.example.btremote.compose.Screens
import com.example.btremote.database.cmd.CMDSend
import com.example.btremote.tools.EasyDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.concurrent.thread

const val HEX_DATA = 0
const val STRING_DATA = 1

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    lateinit var requestLauncher: ActivityResultLauncher<Intent>
    val sendTypeFlow = MutableStateFlow(HEX_DATA)
    val recTypeFlow = MutableStateFlow(HEX_DATA)

    val nowScreenLiveData = MutableLiveData(Screens.mainScreen)
}