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


@HiltViewModel
class WaveViewModel @Inject constructor() : ViewModel() {

}