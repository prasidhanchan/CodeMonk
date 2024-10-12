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

data class User(
    var username: String = "",
    var name: String = "",
    var userId: String = "",
    var token: String = "",
    var bio: String = "",
    var profileImage: String = "",
    var email: String = "",
    var lastSeen: Long = 0L,
    var currentProject: String = "",
    var currentProjectProgress: Int = 0,
    var gitHubLink: String = "",
    var linkedInLink: String = "",
    var portfolioLink: String = "",
    var xp: Int = 0,
    var attendance: Int = 0,
    var semester: String = "1",
    var isVerified: Boolean = false,
    var userType: String = "",
    var mentor: String = "",
    var mentorFor: String = ""
)
