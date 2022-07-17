package com.example.btremote.compose.tab

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.SemanticsProperties.Text
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*

@Composable
fun BottomTab(
    currentScreenId: String,
    onItemSelected: (Screen) -> Unit
) {
    val items = Screen.Item.list

    Row(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,

        ) {
        items.forEach {
            BottomTabItem(item = it, isSelected = it.id == currentScreenId) {
                onItemSelected(it)
            }
        }
    }
}

@Composable
fun BottomTabItem(item: Screen, isSelected: Boolean, onClick: () -> Unit) {

    val background = if (isSelected) MaterialTheme.colors.primary.copy(alpha = 0.1f) else Color.Transparent
    val contentColor = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground

    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(background, shape = RoundedCornerShape(25.dp))
            .clickable(onClick = onClick)
            .width(100.dp)
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = item.icon, contentDescription = null,
            tint = contentColor
        )
        AnimatedVisibility(visible = isSelected) {
            Text(text = item.title, color = contentColor, fontSize = 11.sp)
        }
    }
}