package com.example.btremote

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.btremote.app.App
import com.example.btremote.compose.remoteActivity.RemoteCompose
import com.example.btremote.protocol.sendRemoteVal
import com.example.btremote.ui.theme.BTRemoteTheme
import com.example.btremote.viewmodel.RemoteViewModel
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.util.*

class RemoteActivity : ComponentActivity() {
    private val timer = Timer()
    private val timerTask = object : TimerTask() {
        override fun run() {
            sendRemoteVal()
        }
    }

    private lateinit var orientation: Orientation

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            BTRemoteTheme {
                rememberSystemUiController().setSystemBarsColor(
                    Color.Transparent,
                    darkIcons = MaterialTheme.colors.isLight
                )
                ProvideWindowInsets {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        RemoteCompose()
                    }
                }
            }
        }
        val model: RemoteViewModel by viewModels()
        timer.schedule(timerTask, 1000, 20)
        orientation = Orientation.instance(this)

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = window.insetsController
            controller?.hide(WindowInsetsCompat.Type.systemBars())
            controller?.systemBarsBehavior = BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            val decorView: View = window.decorView
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
        orientation.unregister()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onResume() {
        super.onResume()
        App.wifiService.getNetStatus()
    }

}