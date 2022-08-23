package com.example.btremote.viewmodel

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class RemoteViewModel :ViewModel(){
    lateinit var requestLauncher: ActivityResultLauncher<Intent>
}