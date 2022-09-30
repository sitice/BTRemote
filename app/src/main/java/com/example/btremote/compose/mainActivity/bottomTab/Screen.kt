package com.example.btremote.compose.mainActivity.bottomTab

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.btremote.compose.Screens

sealed class Screen(
    val id:String,
    val title:String,
    val icon:ImageVector
){
    object HomeScreen:Screen(Screens.mainScreen,"首页", Icons.Default.Home)
    object AdvanceScreen:Screen(Screens.moreScreen,"高级", Icons.Default.Star)
    object DataScreen:Screen(Screens.recSendScreen,"收发", Icons.Default.Send)

    object Item{
        val list = listOf(
            HomeScreen,DataScreen,AdvanceScreen
        )
    }
}
