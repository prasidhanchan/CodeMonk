package com.mca.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mca.ui.theme.dosis
import com.mca.ui.theme.fontColor

/**
 * Regular AppBAr composable for all screens.
 */
@Composable
fun CMRegularAppBar(
    text: String,
    modifier: Modifier = Modifier,
    enableBackButton: Boolean = true,
    onBackClick: () -> Unit = { }
) {
    Row(
        modifier = modifier
            .padding(
                top = 10.dp,
                bottom = 30.dp,
                start = 10.dp,
                end = 10.dp
            )
            .fillMaxWidth()
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (enableBackButton) BackButton(onClick = onBackClick)
        Text(
            text = text,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = dosis,
                color = fontColor,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.weight(1f)
        )
        if (enableBackButton) Spacer(modifier = Modifier.width(36.dp))
    }
}

@Preview
@Composable
private fun CmRegularAppbarPreview() {
    CMRegularAppBar(text = "Forgot Password")
}