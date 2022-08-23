package com.example.btremote.lifecycle

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.btremote.MainActivity
import com.example.btremote.app.App
import com.example.btremote.database.cmd.CMDSend
import com.example.btremote.tools.EasyDataStore
import com.example.btremote.tools.LogUtil
import com.example.btremote.tools.ToastUtil
import com.example.btremote.viewmodel.HEX_DATA
import com.example.btremote.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.concurrent.thread

const val REQUEST_ENABLE_BT = 1

var permissionWrite = false

class MainLifecycle(
    private val viewModel: MainViewModel,
    private val activity: MainActivity,
    private val lifecycle: Lifecycle
) : DefaultLifecycleObserver {

    private lateinit var requestMultiplePermissions: ActivityResultLauncher<Array<String>>

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        viewModel.requestLauncher =
            activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    //App.bluetoothStateFlow.value = true
                }
            }
        requestMultiplePermissions =
            activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { it ->
                it.entries.forEach {
                    if (it.key == Manifest.permission.BLUETOOTH) {
                        if (it.value) {
                            ToastUtil.toast(activity, "授权成功！")
                        } else {
                            ToastUtil.toast(activity, "授权失败！")
                        }
                    }
                    if (it.key == Manifest.permission.BLUETOOTH_ADMIN) {
                        if (it.value) {
                            ToastUtil.toast(activity, "授权成功！")
                        } else {
                            ToastUtil.toast(activity, "授权失败！")
                        }
                    }
                    if (it.key == Manifest.permission.ACCESS_COARSE_LOCATION) {
                        if (it.value) {
                            ToastUtil.toast(activity, "授权成功！")
                        } else {
                            ToastUtil.toast(activity, "授权失败！")
                        }
                    }
                    if (it.key == Manifest.permission.ACCESS_FINE_LOCATION) {
                        if (it.value) {
                            ToastUtil.toast(activity, "授权成功！")
                        } else {
                            ToastUtil.toast(activity, "授权失败！")
                        }
                    }
                    if (it.key == Manifest.permission.BLUETOOTH_CONNECT) {
                        if (it.value) {
                            ToastUtil.toast(activity, "授权成功！")
                        } else {
                            ToastUtil.toast(activity, "授权失败！")
                        }
                    }
                    if (it.key == Manifest.permission.BLUETOOTH_SCAN) {
                        if (it.value) {
                            ToastUtil.toast(activity, "授权成功！")
                        } else {
                            ToastUtil.toast(activity, "授权失败！")
                        }
                    }
                    if (it.key == Manifest.permission.CHANGE_WIFI_STATE) {
                        if (it.value) {
                            ToastUtil.toast(activity, "授权成功！")
                        } else {
                            ToastUtil.toast(activity, "授权失败！")
                        }
                    }
                    if (it.key == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                        if (it.value) {
                            permissionWrite = true
                            ToastUtil.toast(activity, "授权成功！")
                        } else {
                            ToastUtil.toast(activity, "授权失败！")
                        }
                    }
                }
            }
        checkPermission()
    }

    private fun checkPermission() {

        val result = ArrayList<String>()

        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity, Manifest.permission
                        .ACCESS_COARSE_LOCATION
                )
            ) {
                //选择不开启权限的时候，提示用户
                ToastUtil.toast(activity, "请开通位置相关权限，否则无法正常使用本应用！")
            }
            //申请权限
            result.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity, Manifest.permission
                        .ACCESS_FINE_LOCATION
                )
            ) {
                //选择不开启权限的时候，提示用户
                ToastUtil.toast(activity, "请开通位置相关权限，否则无法正常使用本应用！")
            }
            //申请权限
            result.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity, Manifest.permission
                        .WRITE_EXTERNAL_STORAGE
                )
            ) {
                //选择不开启权限的时候，提示用户
                ToastUtil.toast(activity, "请开通位置相关权限，否则无法正常使用本应用！")
            }
            //申请权限
            result.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            permissionWrite = true
        }

        requestMultiplePermissions.launch(result.toTypedArray())

    }

}