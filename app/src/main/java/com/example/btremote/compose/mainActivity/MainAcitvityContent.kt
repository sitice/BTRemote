package com.example.btremote.compose.mainActivity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.btremote.R
import com.example.btremote.RemoteActivity
import com.example.btremote.WaveDisplayActivity
import com.example.btremote.app.App
import com.example.btremote.compose.*
import com.example.btremote.compose.connect.ConnectBar
import com.example.btremote.compose.connect.ShowBluetoothDevice
import com.example.btremote.compose.mainActivity.moreScreen.dfprotocol.DFProtocol
import com.example.btremote.compose.mainActivity.moreScreen.esp32cam.Esp32Cam
import com.example.btremote.compose.mainActivity.bottomTab.BottomTab
import com.example.btremote.compose.mainActivity.bottomTab.Screen
import com.example.btremote.compose.mainActivity.dataScreen.DataScreen
import com.example.btremote.compose.mainActivity.mainScreen.MainScreen
import com.example.btremote.compose.mainActivity.mainScreen.NoMoreListenMusic
import com.example.btremote.compose.mainActivity.moreScreen.AdvanceScreen
import com.example.btremote.compose.mainActivity.product.*
import com.example.btremote.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class MainActivityState(
    val navController: NavHostController,
    val scaffoldState: ScaffoldState,
    private val scope: CoroutineScope,
) {
    fun onScaffoldButtonClick() {
        scope.launch {
            scaffoldState.drawerState.apply {
                if (isClosed) open() else close()
            }
        }
    }

    var isOpenBottomSheet by mutableStateOf(true)
    var isShowTopBar by mutableStateOf(true)

}

@Composable
fun rememberMainActivityState(
    navController: NavHostController = rememberNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    scope: CoroutineScope = rememberCoroutineScope(),
    resources: Resources = LocalContext.current.resources
) = remember {
    MainActivityState(navController, scaffoldState, scope)
}

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun MainActivityScaffold(
    context: Context = LocalContext.current,
    model: MainViewModel = viewModel()
) {
    val state = rememberMainActivityState()

    var openWifiDialog by remember {
        mutableStateOf(false)
    }
    var openBluetoothDialog by remember {
        mutableStateOf(false)
    }
    var openUSBDialog by remember {
        mutableStateOf(false)
    }
    var screen by remember {
        mutableStateOf<Screen>(Screen.HomeScreen)
    }

    var openNoMoreListenMusic by remember {
        mutableStateOf(false)
    }

    if (openNoMoreListenMusic) {
        NoMoreListenMusic(
            text = "There's no more,choose a band to listen to music",
            listOf("Zed Zeppelin", "Pink Floyd", "The Beatles", "万能青年旅店"),
            onClick = { band ->
                val uri = when (band) {
                    "Zed Zeppelin" -> model.stairwayToHeaven
                    "Pink Floyd" -> model.theDarkSideOfTheMoon
                    "The Beatles" -> model.strawberryFieldForever
                    "万能青年旅店" -> model.qinHuangDao
                    else -> model.stairwayToHeaven
                }
                openNoMoreListenMusic = false
                model.openUri(uri,context)
            })
    }
    if (openBluetoothDialog) {
        App.bluetoothService.startScan()
        ShowBluetoothDevice({
            openBluetoothDialog = false
        }, {
            //蓝牙开关回调
            if (it) {
                model.openBluetooth(context)
            } else {
                model.closeBluetooth()
            }
        })
    }
//    if (openWifiDialog)
//        ShowWifiDevice(onOpenStateChange = { openWifiDialog = false })
//
//    if (openUSBDialog)
//        ShowUSBDevice(onOpenStateChange = { openUSBDialog = false })

    Scaffold(
        backgroundColor = Color(0xffeff3f6),
        scaffoldState = state.scaffoldState,
        topBar = {
            if (state.isShowTopBar)
                TopAppBar(modifier = Modifier.height(50.dp), title = {
                    Text(
                        text = "BTRemote",
                        fontFamily = FontFamily.Cursive,
                        fontWeight = FontWeight(700)
                    )
                }, backgroundColor = Color.White, navigationIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_menu_black_24dp),
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            state.onScaffoldButtonClick()
                        })
                }, actions = {
                    ConnectBar(
                        onOpenBluetoothButtonClick = { openBluetoothDialog = true },
                        onOpenWIFIButtonClick = { openWifiDialog = true },
                        onOpenUSBButtonClick = { openUSBDialog = true },
                        isDark = false
                    )
                })

        },
        bottomBar = {
            if (state.isOpenBottomSheet) {
                BottomTab(currentScreenId = screen.id) {
                    screen = it
                }
            }
        },
        drawerContent = {
            DividerContentCompose({
                model.nowScreen = it
                when (it) {
                    Screens.waveDisplayScreen -> context.startActivity(
                        Intent(
                            context,
                            WaveDisplayActivity::class.java
                        )
                    )
                    Screens.remoteScreen -> context.startActivity(
                        Intent(
                            context,
                            RemoteActivity::class.java
                        )
                    )
                    Screens.mainScreen -> {
                        screen = Screen.HomeScreen
                        state.navController.navigate(Screens.mainScreen)
                    }
                    Screens.recSendScreen -> {
                        screen = Screen.DataScreen
                        state.navController.navigate(Screens.mainScreen)
                    }
                    Screens.moreScreen -> {
                        screen = Screen.AdvanceScreen
                        state.navController.navigate(Screens.mainScreen)
                    }
                    else -> {
                        state.navController.navigate(it)
                    }
                }
                state.onScaffoldButtonClick()
            })
        },
    ) { padding ->
        NavHost(
            state.navController,
            startDestination = Screen.HomeScreen.id,
            Modifier.padding(padding)
        ) {
            composable(Screen.HomeScreen.id) {
                model.nowScreen = Screen.HomeScreen.id
                state.isShowTopBar = true
                state.isOpenBottomSheet = true
                when (screen) {
                    Screen.HomeScreen -> MainScreen(
                        navController = state.navController,
                        onMoreClick = { openNoMoreListenMusic = true })
                    Screen.DataScreen -> DataScreen()
                    Screen.AdvanceScreen -> AdvanceScreen(navController = state.navController)
                }
            }
            composable(Screens.productScreen) {
                model.nowScreen = Screens.productScreen
                state.isShowTopBar = true
                state.isOpenBottomSheet = false
                ProductScreen(state.navController)
            }
            composable(Screens.productSettingScreen) {
                model.nowScreen = Screens.productSettingScreen
                state.isShowTopBar = true
                state.isOpenBottomSheet = true
                ProductSetting()
            }
            composable(Screens.settingScreen) {
                model.nowScreen = Screens.settingScreen
                state.isShowTopBar = true
                state.isOpenBottomSheet = true
            }
            composable(Screens.aboutScreen) {
                model.nowScreen = Screens.aboutScreen
                state.isShowTopBar = true
                state.isOpenBottomSheet = false
                About()
            }
            composable(Screens.debugScreen) {
                model.nowScreen = Screens.debugScreen
                state.isShowTopBar = true
                state.isOpenBottomSheet = true
                DebugCompose()
            }
            composable(Screens.protocolScreen) {
                model.nowScreen = Screens.protocolScreen
                state.isShowTopBar = true
                state.isOpenBottomSheet = true
                DFProtocol()
            }
            composable(Screens.esp32camScreen) {
                model.nowScreen = Screens.esp32camScreen
                state.isShowTopBar = true
                state.isOpenBottomSheet = false
                Esp32Cam()
            }
        }
    }
}





