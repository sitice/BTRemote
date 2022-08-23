package com.example.btremote.compose

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.btremote.MainActivity
import com.example.btremote.R
import com.example.btremote.app.App
import com.example.btremote.app.BLUETOOTHStatus
import com.example.btremote.app.WIFIStatus
import com.example.btremote.compose.about.About
import com.example.btremote.compose.baseSendRec.BaseSendRec
import com.example.btremote.compose.dfprotocol.DFProtocol
import com.example.btremote.compose.esp32cam.Esp32Cam
import com.example.btremote.compose.tab.BottomTab
import com.example.btremote.compose.tab.Screen
import com.example.btremote.compose.more.MoreShowCompose
import com.example.btremote.compose.product.*
import com.example.btremote.lifecycle.REQUEST_ENABLE_BT
import com.example.btremote.tools.ToastUtil
import com.example.btremote.viewmodel.MainViewModel
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun MainActivityScaffold(context: Context = LocalContext.current) {

    val model: MainViewModel = viewModel()

    val navController = rememberNavController()



    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val openWifiDialog = remember {
        mutableStateOf(false)
    }
    val openBluetoothDialog = remember {
        mutableStateOf(false)
    }

    val openUSBDialog = remember {
        mutableStateOf(false)
    }

    val nowScreen = remember {
        mutableStateOf<Screen>(Screen.HomeScreen)
    }

    val isOpenBottomSheet = remember {
        mutableStateOf(true)
    }

    val isShowTopBar = remember {
        mutableStateOf(true)
    }

    ShowBluetoothDevice(openDialog = openBluetoothDialog) {
        if (it) {
            val bluetoothAdapter: BluetoothAdapter = App.bluetoothService.mManager.adapter
            if (!bluetoothAdapter.isEnabled) {
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                model.requestLauncher.launch(intent)
                val activity = context as MainActivity
                activity.setResult(REQUEST_ENABLE_BT)
            }
        } else {
            val bluetoothAdapter: BluetoothAdapter = App.bluetoothService.mManager.adapter
            if (bluetoothAdapter.isEnabled) {
                bluetoothAdapter.disable()
            }
        }
    }
    ShowWifiDevice(openDialog = openWifiDialog)
    ShowUSBDevice(openDialog = openUSBDialog)
    Scaffold(
        backgroundColor = Color(0xffeff3f6),
        scaffoldState = scaffoldState,
        topBar = {
            if (isShowTopBar.value)
                TopAppBar(modifier = Modifier.height(50.dp), title = {
                    Text(
                        text = "DFRemote",
                        fontFamily = FontFamily.Cursive,
                        fontWeight = FontWeight(700)
                    )
                }, backgroundColor = Color.White, navigationIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_menu_black_24dp),
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            scope.launch {
                                scaffoldState.drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        })
                }, actions = {
                    ConnectBar(openBluetoothDialog,openWifiDialog,openUSBDialog,false)
                })

        },
        bottomBar = {
            if (isOpenBottomSheet.value) {
                BottomTab(currentScreenId = nowScreen.value.id) {
                    nowScreen.value = it
                }
            }
        },
        drawerContent = {
            DividerContentCompose(navController, scope, scaffoldState, nowScreen)
        },
    ) { padding ->
        NavHost(navController, startDestination = Screen.HomeScreen.id, Modifier.padding(padding)) {
            composable(Screen.HomeScreen.id) {
                model.nowScreenLiveData.value = Screens.mainScreen
                isOpenBottomSheet.value = true
                isShowTopBar.value = true
                if (nowScreen.value == Screen.HomeScreen) {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        Banner()
                        MoreShowCompose()
                        ProductListCompose(navController)
                    }
                } else if (nowScreen.value == Screen.DataScreen) {
                    AdvanceScreen(navController)
                    model.nowScreenLiveData.value = Screens.moreScreen

                } else if (nowScreen.value == Screen.ViewScreen) {
                    model.nowScreenLiveData.value = Screens.recSendScreen
                    BaseSendRec()
                }

            }
            composable(Screens.productScreen) {
                model.nowScreenLiveData.value = Screens.productScreen
                isOpenBottomSheet.value = false
                isShowTopBar.value = true
                ProductScreen(navController)
            }
            composable(Screens.settingScreen) {
                model.nowScreenLiveData.value = Screens.settingScreen
                isOpenBottomSheet.value = false
                isShowTopBar.value = true
                Setting()
            }
            composable(Screens.aboutScreen) {
                model.nowScreenLiveData.value = Screens.aboutScreen
                isOpenBottomSheet.value = false
                isShowTopBar.value = true
                About()
            }
            composable(Screens.debugScreen) {
                model.nowScreenLiveData.value = Screens.debugScreen
                isOpenBottomSheet.value = false
                isShowTopBar.value = true
                DebugCompose()
            }
            composable(Screens.protocolScreen) {
                model.nowScreenLiveData.value = Screens.protocolScreen
                isOpenBottomSheet.value = false
                isShowTopBar.value = true
                DFProtocol()
            }
            composable(Screens.esp32camScreen) {
                model.nowScreenLiveData.value = Screens.esp32camScreen
                isOpenBottomSheet.value = false
                isShowTopBar.value = true
                Esp32Cam()
            }
        }
    }

}





