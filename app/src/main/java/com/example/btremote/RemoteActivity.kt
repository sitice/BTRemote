package com.example.btremote

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowInsetsCompat
import com.example.btremote.compose.remote.RemoteCompose
import com.example.btremote.lifecycle.MainLifecycle
import com.example.btremote.lifecycle.RemoteLifecycle
import com.example.btremote.tools.WindowManager
import com.example.btremote.ui.theme.BTRemoteTheme
import com.example.btremote.viewmodel.MainViewModel
import com.example.btremote.viewmodel.RemoteViewModel
import com.google.accompanist.insets.ProvideWindowInsets

class RemoteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BTRemoteTheme {
                ProvideWindowInsets {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        RemoteCompose()
                    }
                }
            }
        }
        val model: RemoteViewModel by viewModels()
        lifecycle.addObserver(RemoteLifecycle(viewModel = model, activity = this))
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
}