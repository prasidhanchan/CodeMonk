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

package com.mca.util.model

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class User(
    val username: String = "",
    val name: String = "",
    val userId: String = "",
    val token: String = "",
    val bio: String = "",
    val profileImage: String = "",
    val email: String = "",
    val lastSeen: Long = 0L,
    val currentProject: String = "",
    val currentProjectProgress: Int = 0,
    val gitHubLink: String = "",
    val linkedInLink: String = "",
    val portfolioLink: String = "",
    val xp: Int = 0,
    val attendance: Int = 0,
    val semester: String = "1",
    val isVerified: Boolean = false,
    val userType: String = "",
    val mentor: String = "",
    val mentorFor: String = ""
)