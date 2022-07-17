package com.example.btremote.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import kotlin.math.absoluteValue
import com.example.btremote.R
import com.example.btremote.ui.theme.roundedCorner10dp
import com.google.accompanist.pager.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Banner(modifier: Modifier = Modifier) {

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
            modifier = Modifier.padding(start = 30.dp, end = 30.dp, top = 20.dp, bottom = 10.dp)
               /* .graphicsLayer {
                    val pageOffset = calculateCurrentOffsetForPage(it).absoluteValue

                    // We animate the scaleX + scaleY, between 85% and 100%
                    lerp(
                        start = 0.9f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    ).also { scale ->
                        scaleX = scale
                        scaleY = scale
                    }
                    // We animate the alpha, between 50% and 100%
                    alpha = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f),
                    )
                }
                .aspectRatio(1.6f, true),*/,
            shape = roundedCorner10dp
        ) {
            Box {
                Image(
                    painter = when (currentPage) {
                        0 -> painterResource(id = R.drawable.screenshot_20220610_172647)
                        else -> painterResource(id = R.drawable.screenshot_20220610_223252)
                    },
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillHeight
                )
            }
        }

    }
    Column(modifier = Modifier.fillMaxWidth()) {
        // 水平指示器
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
               ,
        )

    }
    LaunchedEffect(pagerState) {
        while (true) {
            delay(5000)
            val page = if (pagerState.currentPage >= pagerState.pageCount - 1) 0 else pagerState.currentPage + 1
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
