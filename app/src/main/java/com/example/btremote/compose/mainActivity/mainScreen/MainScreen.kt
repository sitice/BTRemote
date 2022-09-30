package com.example.btremote.compose.mainActivity.mainScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun MainScreen(navController:NavHostController,onMoreClick:()->Unit) {
    Column {
        Banner()
        WaveAndRemote()
        ProductList(navController = navController,onMoreClick)
    }
}