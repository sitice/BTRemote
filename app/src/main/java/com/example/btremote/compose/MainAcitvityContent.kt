package com.example.btremote.compose

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.btremote.MainActivity
import com.example.btremote.R
import com.example.btremote.app.App
import com.example.btremote.app.BLUETOOTHStatus
import com.example.btremote.compose.about.About
import com.example.btremote.compose.baseSendRec.BaseSendRec
import com.example.btremote.compose.tab.BottomTab
import com.example.btremote.compose.tab.Screen
import com.example.btremote.compose.more.MoreShowCompose
import com.example.btremote.compose.product.*
import com.example.btremote.compose.remote.RemoteCompose
import com.example.btremote.compose.waveDisplay.WaveDisplayCompose
import com.example.btremote.tools.WindowManager
import com.example.btremote.viewmodel.MainViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun MainActivityScaffold(isProductScreen: MutableState<Boolean>, activity: MainActivity) {

    val model: MainViewModel = viewModel()

    val navController = rememberNavController()

    val bluetoothStatus = App.bluetoothStateFlow.collectAsState()

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val openWifiDialog = remember {
        mutableStateOf(false)
    }
    val openBluetoothDialog = remember {
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


    ShowBluetoothDevice(openDialog = openBluetoothDialog)
    ShowWifiDevice(openDialog = openWifiDialog)

    Scaffold(
        backgroundColor = if (isProductScreen.value) Color(0xff424250) else Color(0xffeff3f6),
        scaffoldState = scaffoldState,
        topBar = {
            if (isShowTopBar.value)
                TopAppBar(title = {
                    Text(text = "DFRemote", fontFamily = FontFamily.Cursive, fontWeight = FontWeight(700))
                }, backgroundColor = if (isProductScreen.value) Color(0xff0e2441) else Color.White, navigationIcon = {
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

                    IconButton(onClick = { openBluetoothDialog.value = true }) {
                        Icon(
                            painter = painterResource(
                                id = when (bluetoothStatus.value) {
                                    BLUETOOTHStatus.DISABLE -> R.drawable.baseline_bluetooth_disabled_black_24dp
                                    BLUETOOTHStatus.DISCONNECTED -> R.drawable.baseline_bluetooth_black_24dp
                                    BLUETOOTHStatus.CONNECTED -> R.drawable.baseline_bluetooth_connected_black_24dp
                                    else -> {
                                        R.drawable.baseline_bluetooth_black_24dp
                                    }
                                }
                            ), contentDescription = null
                        )
                    }
                    IconButton(onClick = { openWifiDialog.value = true }) {
                        Icon(painter = painterResource(id = R.drawable.baseline_signal_wifi_off_black_24dp), contentDescription = null)

                    }
                    Spacer(modifier = Modifier.width(20.dp))
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
            DividerContentCompose(navController, scope, scaffoldState, nowScreen,activity)
        },
    ) { padding ->
        NavHost(navController, startDestination = Screen.HomeScreen.id, Modifier.padding(padding)) {
            composable(Screen.HomeScreen.id) {
                model.nowScreenLiveData.value = Screens.mainScreen
                isOpenBottomSheet.value = true
                isProductScreen.value = false
                isShowTopBar.value = true
                if (nowScreen.value == Screen.HomeScreen) {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        Banner()
                        MoreShowCompose()
                        ProductListCompose()
                        //DeviceCompose(navController)

                    }
                } else if (nowScreen.value == Screen.DataScreen) {
                    //MoreShowCompose()
                    model.nowScreenLiveData.value = Screens.moreScreen

                } else if (nowScreen.value == Screen.ViewScreen) {
                    model.nowScreenLiveData.value = Screens.recSendScreen
                    BaseSendRec()
                }

            }
            composable(Screens.productScreen) {
                model.nowScreenLiveData.value = Screens.productScreen
                isOpenBottomSheet.value = false
                isProductScreen.value = true
                isShowTopBar.value = true
                ProductScreen(navController)
            }
            composable(Screens.settingScreen) {
                model.nowScreenLiveData.value = Screens.settingScreen
                isOpenBottomSheet.value = false
                isProductScreen.value = false
                isShowTopBar.value = true
                Setting()
            }
            composable(Screens.aboutScreen) {
                model.nowScreenLiveData.value = Screens.aboutScreen
                isOpenBottomSheet.value = false
                isProductScreen.value = false
                isShowTopBar.value = true
                About()
            }
            composable(Screens.debugScreen) {
                model.nowScreenLiveData.value = Screens.debugScreen
                isOpenBottomSheet.value = false
                isProductScreen.value = false
                isShowTopBar.value = true
                DebugCompose()
            }
        }
    }

}





