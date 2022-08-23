package com.example.btremote

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.btremote.app.App
import com.example.btremote.compose.MainActivityScaffold
import com.example.btremote.lifecycle.MainLifecycle
import com.example.btremote.tools.WindowManager
import com.example.btremote.ui.theme.BTRemoteTheme
import com.example.btremote.viewmodel.MainViewModel
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            BTRemoteTheme {
                ProvideWindowInsets {
                    val systemUiController = rememberSystemUiController()

                    SideEffect {
                            systemUiController.setSystemBarsColor(
                                color =Color.White,
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
        lifecycle.addObserver(MainLifecycle(viewModel = model, activity = this,lifecycle))
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onResume() {
        super.onResume()
        App.wifiService.getNetStatus()
    }

}
