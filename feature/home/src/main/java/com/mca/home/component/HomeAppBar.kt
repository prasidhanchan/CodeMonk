package com.mca.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mca.ui.R
import com.mca.ui.theme.LightBlack
import com.mca.ui.theme.dosis
import com.mca.ui.theme.fontColor
import com.mca.ui.theme.tintColor

/**
 * Home AppBar for Home Screen.
 */
@Composable
fun HomeAppBar(
    modifier: Modifier = Modifier,
    userImage: String,
    onSearchClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(vertical = 20.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = R.drawable.codemonk_logo),
            contentDescription = stringResource(id = R.string.app_name),
            modifier = Modifier.size(45.dp)
        )
        Text(
            text = stringResource(id = R.string.app_name),
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = dosis,
                color = fontColor
            ),
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .weight(1f)
        )
        IconButton(onClick = onSearchClick) {
            Icon(
                painter = painterResource(id = R.drawable.search),
                contentDescription = stringResource(id = R.string.search),
                tint = tintColor
            )
        }
        AsyncImage(
            model = userImage,
            contentDescription = stringResource(id = R.string.profile),
            modifier = Modifier
                .clip(CircleShape)
                .size(35.dp)
                .background(LightBlack)
                .clickable(onClick = onProfileClick)
        )
    }
}

@Preview
@Composable
private fun HomeAppBarPreview() {
    HomeAppBar(
        userImage = "",
        onSearchClick = { },
        onProfileClick = { }
    )
}