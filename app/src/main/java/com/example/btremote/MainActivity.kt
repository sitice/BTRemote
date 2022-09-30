package com.example.btremote

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.example.btremote.app.App
import com.example.btremote.compose.mainActivity.MainActivityScaffold
import com.example.btremote.lifecycle.MainLifecycle
import com.example.btremote.ui.theme.BTRemoteTheme
import com.example.btremote.viewmodel.MainViewModel
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.DelicateCoroutinesApi

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            BTRemoteTheme {
                ProvideWindowInsets {
                    val systemUiController = rememberSystemUiController()

                    SideEffect {
                        systemUiController.setSystemBarsColor(
                            color = Color.White,
                            darkIcons = true
                        )
                    }
                    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
                        MainActivityScaffold()
                    }
                }
            }
        }
        val model: MainViewModel by viewModels()
        model.requestLauncher =
            this.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    //根据广播可以知道蓝牙状态
                    //App.bluetoothStateFlow.value = true
                }
            }
        lifecycle.addObserver(MainLifecycle(viewModel = model, activity = this, lifecycle))

    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onResume() {
        super.onResume()
        App.wifiService.getNetStatus()
    }

}
