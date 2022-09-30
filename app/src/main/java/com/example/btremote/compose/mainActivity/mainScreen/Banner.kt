package com.example.btremote.compose.mainActivity.mainScreen

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.math.absoluteValue
import com.example.btremote.R
import com.example.btremote.ui.theme.roundedCorner10dp
import com.example.btremote.viewmodel.MainViewModel
import com.google.accompanist.pager.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Banner(
    modifier: Modifier = Modifier,
    model: MainViewModel = viewModel(),
    context: Context = LocalContext.current
) {
    val pagerState = rememberPagerState(
        //初始页面
        initialPage = 0
    )
    HorizontalPager(
        count = 4,
        contentPadding = PaddingValues(horizontal = 0.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp),
        state = pagerState
    ) {
        Card(
            modifier = Modifier
                .padding(start = 30.dp, end = 30.dp, top = 20.dp, bottom = 10.dp)
                .clickable {
                    val uri = when (currentPage) {
                        0 -> model.stairwayToHeaven
                        1 -> model.theDarkSideOfTheMoon
                        2 -> model.strawberryFieldForever
                        3 -> model.qinHuangDao
                        else -> model.qinHuangDao
                    }
                    model.openUri(uri,context)
                },
            shape = roundedCorner10dp
        ) {
            Box {
                Image(
                    painter = when (currentPage) {
                        0 -> painterResource(id = R.drawable.zed_zeppelin2)
                        1 -> painterResource(id = R.drawable.pink_floyd1)
                        2 -> painterResource(id = R.drawable.the_beatles1)
                        3 -> painterResource(id = R.drawable.wanqin1)
                        else -> painterResource(id = R.drawable.wanqin1)
                    },
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillWidth
                )
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(10.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color.White
                    ) {
                        Text(
                            modifier = Modifier.padding(start = 5.dp, end = 5.dp),
                            text = when (currentPage) {
                                0 -> "Zed Zeppelin"
                                1 -> "Pink Floyd"
                                2 -> "The Beatles"
                                3 -> "万能青年旅店"
                                else -> "万能青年旅店"
                            },
                            color = Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight(500)
                        )
                    }
                    Spacer(modifier = modifier.width(5.dp))
                }
            }
        }

    }
    Column(modifier = Modifier.fillMaxWidth()) {
        // 水平指示器
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
        )

    }
    LaunchedEffect(pagerState) {
        while (true) {
            delay(5000)
            val page =
                if (pagerState.currentPage >= pagerState.pageCount - 1) 0 else pagerState.currentPage + 1
            pagerState.animateScrollToPage(page)
        }

    }

}


@Composable
private fun ProfilePicture(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = CircleShape,
        border = BorderStroke(4.dp, MaterialTheme.colors.surface)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = null,
            modifier = Modifier.size(72.dp),
        )
    }
}
