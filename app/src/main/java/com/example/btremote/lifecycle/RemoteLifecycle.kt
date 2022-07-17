package com.example.btremote.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.example.btremote.RemoteActivity
import com.example.btremote.database.viewpos.ViewPosAndGestureDatabase
import com.example.btremote.viewmodel.RemoteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RemoteLifecycle(
    private val viewModel: RemoteViewModel,
    private val activity: RemoteActivity
) : DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            val dao = ViewPosAndGestureDatabase.getInstance(activity).viewPosAndGestureDao()
            viewModel.viewPosAndGesturesFlow.value = dao.getViewPosAndGestures()
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)

    }
}