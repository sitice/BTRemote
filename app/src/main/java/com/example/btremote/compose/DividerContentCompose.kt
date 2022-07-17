package com.example.btremote.compose

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.btremote.R
import com.example.btremote.RemoteActivity
import com.example.btremote.WaveDisplayActivity
import com.example.btremote.compose.tab.Screen
import com.example.btremote.ui.theme.Blue
import com.example.btremote.ui.theme.LightBlue
import com.example.btremote.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun openNewScreen(id: String, navController: NavHostController, scope: CoroutineScope, scaffoldState: ScaffoldState) {
    navController.navigate(id) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
    scope.launch {
        scaffoldState.drawerState.apply {
            if (isClosed) open() else close()
        }
    }
}

@Composable
fun DividerContentCompose(
    navController: NavHostController,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    screen: MutableState<Screen>,
    activity: Activity
) {
    val model: MainViewModel = viewModel()
    val screenState = model.nowScreenLiveData.asFlow().collectAsState(initial = Screens.mainScreen)

    Text("已连接设备", modifier = Modifier.padding(16.dp))
    Divider()
    Spacer(modifier = Modifier.height(10.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(start = 16.dp, end = 16.dp)
            .background(color = if (screenState.value == Screens.productScreen) LightBlue else Color.White, shape = RoundedCornerShape(5.dp))
            .clickable {
                openNewScreen(Screens.productScreen, navController, scope, scaffoldState)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(30.dp)
                .padding(start = 10.dp),
            painter = painterResource(R.drawable.df_car),
            contentDescription = null, tint = if (screenState.value == Screens.productScreen) Blue else Color.Black
        )
        Spacer(modifier = Modifier.width(30.dp))
        Text(text = "点浮战车", color = if (screenState.value == Screens.productScreen) Blue else Color.Black)
    }


    Spacer(modifier = Modifier.height(10.dp))
    Divider()
    Spacer(modifier = Modifier.height(10.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(start = 16.dp, end = 16.dp)
            .background(color = if (screenState.value == Screens.mainScreen) LightBlue else Color.White, shape = RoundedCornerShape(5.dp))
            .clickable {

                openNewScreen(Screens.mainScreen, navController, scope, scaffoldState)

            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(30.dp)
                .padding(start = 10.dp),
            painter = painterResource(R.drawable.baseline_home_black_36dp),
            contentDescription = null, tint = if (screenState.value == Screens.mainScreen) Blue else Color.Black
        )
        Spacer(modifier = Modifier.width(30.dp))
        Text(text = "首页", color = if (screenState.value == Screens.mainScreen) Blue else Color.Black)
    }

    Spacer(modifier = Modifier.height(10.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(start = 16.dp, end = 16.dp)
            .background(color = if (screenState.value == Screens.recSendScreen) LightBlue else Color.White, shape = RoundedCornerShape(5.dp))
            .clickable {
                openNewScreen(Screens.mainScreen, navController, scope, scaffoldState)
                screen.value = Screen.ViewScreen
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(30.dp)
                .padding(start = 10.dp),
            painter = painterResource(R.drawable.recandsend),
            contentDescription = null, tint = if (screenState.value == Screens.recSendScreen) Blue else Color.Black
        )
        Spacer(modifier = Modifier.width(30.dp))
        Text(text = "基本收发", color = if (screenState.value == Screens.recSendScreen) Blue else Color.Black)
    }

    Spacer(modifier = Modifier.height(10.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(start = 16.dp, end = 16.dp)
            .background(color = if (screenState.value == Screens.remoteScreen) LightBlue else Color.White, shape = RoundedCornerShape(5.dp))
            .clickable {
                activity.startActivity(Intent(activity, RemoteActivity::class.java))
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(30.dp)
                .padding(start = 10.dp),
            painter = painterResource(R.drawable.baseline_games_black_24dp),
            contentDescription = null, tint = if (screenState.value == Screens.remoteScreen) Blue else Color.Black
        )
        Spacer(modifier = Modifier.width(30.dp))
        Text(text = "遥控", color = if (screenState.value == Screens.remoteScreen) Blue else Color.Black)
    }


    Spacer(modifier = Modifier.height(10.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(start = 16.dp, end = 16.dp)
            .background(color = if (screenState.value == Screens.waveDisplayScreen) LightBlue else Color.White, shape = RoundedCornerShape(5.dp))
            .clickable {
                activity.startActivity(Intent(activity, WaveDisplayActivity::class.java))
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(30.dp)
                .padding(start = 10.dp),
            painter = painterResource(R.drawable.baseline_microwave_black_24dp),
            contentDescription = null, tint = if (screenState.value == Screens.waveDisplayScreen) Blue else Color.Black
        )
        Spacer(modifier = Modifier.width(30.dp))
        Text(text = "波形显示", color = if (screenState.value == Screens.waveDisplayScreen) Blue else Color.Black)
    }


    Spacer(modifier = Modifier.height(10.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(start = 16.dp, end = 16.dp)
            .background(color = if (screenState.value == Screens.settingScreen) LightBlue else Color.White, shape = RoundedCornerShape(5.dp))
            .clickable {
                openNewScreen(Screens.settingScreen, navController, scope, scaffoldState)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(30.dp)
                .padding(start = 10.dp),
            painter = painterResource(R.drawable.baseline_settings_black_24dp),
            contentDescription = null, tint = if (screenState.value == Screens.settingScreen) Blue else Color.Black
        )
        Spacer(modifier = Modifier.width(30.dp))
        Text(text = "设置", color = if (screenState.value == Screens.settingScreen) Blue else Color.Black)
    }


    Spacer(modifier = Modifier.height(10.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(start = 16.dp, end = 16.dp)
            .background(color = if (screenState.value == Screens.aboutScreen) LightBlue else Color.White, shape = RoundedCornerShape(5.dp))
            .clickable {
                openNewScreen(Screens.aboutScreen, navController, scope, scaffoldState)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(30.dp)
                .padding(start = 10.dp),
            painter = painterResource(R.drawable.baseline_info_black_24dp),
            contentDescription = null, tint = if (screenState.value == Screens.aboutScreen) Blue else Color.Black
        )
        Spacer(modifier = Modifier.width(30.dp))
        Text(text = "关于", color = if (screenState.value == Screens.aboutScreen) Blue else Color.Black)
    }
}



