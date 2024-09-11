/*
 * Copyright © 2024 Prasidh Gopal Anchan
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

package com.mca.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import com.mca.ui.theme.Black
import com.mca.ui.theme.LightBlack
import com.mca.util.constant.Constant.ADMIN

/**
 * Loading indicator for Profile Screen.
 */
@Composable
internal fun ProfileScreenLoader(
    visible: Boolean,
    userType: String,
    modifier: Modifier = Modifier
) {
    if (visible) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = modifier
                    .padding(top = 60.dp)
                    .fillMaxSize()
                    .background(color = Black),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Image
                Surface(
                    modifier = Modifier
                        .padding(top = 30.dp, bottom = 10.dp)
                        .size(100.dp),
                    shape = CircleShape,
                    color = LightBlack,
                    content = { }
                )
                // Name
                Surface(
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                        .width(150.dp)
                        .height(20.dp),
                    shape = CircleShape,
                    color = LightBlack,
                    content = { }
                )
                // Bio
                Surface(
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                        .width(200.dp)
                        .height(10.dp),
                    shape = CircleShape,
                    color = LightBlack,
                    content = { }
                )
                // Links
                Row(
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(3) {
                        Surface(
                            modifier = Modifier
                                .padding(vertical = 5.dp, horizontal = 15.dp)
                                .size(30.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = LightBlack,
                            content = { }
                        )
                    }
                }

                // Username
                Surface(
                    modifier = modifier
                        .padding(top = 10.dp, bottom = 10.dp)
                        .height(28.dp)
                        .width(90.dp),
                    shape = CircleShape,
                    color = LightBlack,
                    content = { }
                )
                // Working on/Mentor for
                Surface(
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .width(180.dp)
                        .height(15.dp),
                    shape = CircleShape,
                    color = LightBlack,
                    content = { }
                )
                // Progress Bar
                if (userType != ADMIN) {
                    Spacer(modifier = Modifier.height(10.dp))
                    repeat(2) {
                        Surface(
                            modifier = Modifier
                                .padding(vertical = 15.dp)
                                .fillMaxWidth(0.7f)
                                .height(12.dp),
                            shape = CircleShape,
                            color = LightBlack,
                            content = { }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
                repeat(3) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            modifier = Modifier
                                .padding(vertical = 12.dp)
                                .size(25.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = LightBlack,
                            content = { }
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Surface(
                            modifier = Modifier
                                .padding(vertical = 12.dp)
                                .fillMaxWidth(0.85f)
                                .height(20.dp),
                            shape = RoundedCornerShape(6.dp),
                            color = LightBlack,
                            content = { }
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(horizontal = 28.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Surface(
                        modifier = Modifier
                            .padding(vertical = 12.dp)
                            .size(25.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = LightBlack,
                        content = { }
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Surface(
                        modifier = Modifier
                            .padding(vertical = 12.dp)
                            .fillMaxWidth(0.3f)
                            .height(20.dp),
                        shape = RoundedCornerShape(6.dp),
                        color = LightBlack,
                        content = { }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProfileScreenLoaderPreview() {
    ProfileScreenLoader(
        visible = true,
        userType = ADMIN
    )
}