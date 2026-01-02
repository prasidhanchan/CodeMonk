/*
 * Copyright © 2026 Prasidh Gopal Anchan
 *
 * Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://creativecommons.org/licenses/by-nc-nd/4.0/
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 */

package com.mca.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mca.ui.theme.LightBlack

/**
 * Loading indicator composable to display a post loading indicator.
 */
@Composable
internal fun PostLoader(modifier: Modifier = Modifier) {
    Column(
        modifier
            .padding(vertical = 10.dp)
            .height(455.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Surface(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(30.dp),
                shape = CircleShape,
                color = LightBlack,
                content = { }
            )
            Surface(
                modifier = Modifier
                    .width(80.dp)
                    .height(15.dp),
                shape = CircleShape,
                color = LightBlack,
                content = { }
            )
        }

        Surface(
            modifier = modifier
                .padding(bottom = 5.dp)
                .fillMaxWidth()
                .height(320.dp),
            shape = RoundedCornerShape(15.dp),
            color = LightBlack,
            content = { }
        )
        Surface(
            modifier = modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth(0.9f)
                .height(20.dp),
            shape = CircleShape,
            color = LightBlack,
            content = { }
        )
        Surface(
            modifier = modifier
                .padding(top = 10.dp)
                .width(250.dp)
                .height(15.dp),
            shape = CircleShape,
            color = LightBlack,
            content = { }
        )
    }
}

@Preview
@Composable
private fun PostCardLoaderPreview() {
    PostLoader()
}