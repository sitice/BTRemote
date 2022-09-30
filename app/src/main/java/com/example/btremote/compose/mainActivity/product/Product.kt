package com.example.btremote.compose.mainActivity.product

import androidx.annotation.DrawableRes

sealed class Product(
    name: String, description: String, @DrawableRes
    id: Int
) {
    //object DFCar:Product("点浮战车", "111", null)
    object Item{
        val list = listOf<Product>()
    }
}
