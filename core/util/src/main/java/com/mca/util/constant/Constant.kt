/*
 * Copyright © 2025 Prasidh Gopal Anchan
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

package com.mca.util.constant

object Constant {

    /** Regex for username ex: pra_sidh_22 */
    val USERNAME_REGEX = Regex("^[a-zA-Z_]+[a-zA-Z0-9_]*\$")

    /** Regex for deadline ex: 20 Apr 2024 */
    val DEADLINE_REGEX = Regex("\\d{1,2}\\s(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sept|Oct|Nov|Dec)\\s\\d{4}")

    /** Regex for email */
    val EMAIL_REGEX = Regex("^(nnm)(23|24)(mc)(0[0-9][1-9]|0[1-9][0-9]|1[0-7][0-9]|180)@nmamit\\.in\$")

    /** Admin role variable */
    const val ADMIN = "Admin"

    /** Base url for http v1 */
    const val BASE_URL = "https://fcm.googleapis.com/v1/projects/"

    /** Firebase scope variable */
    const val SCOPE = "https://www.googleapis.com/auth/firebase.messaging"

    const val EVENT_CHANNEL_ID = "EventChannel"
    const val EVENT_TOPIC = "Event"
    const val POST_CHANNEL_ID = "PostChannel"
    const val POST_TOPIC = "Post"
    const val ANNOUNCEMENT_CHANNEL_ID = "AnnouncementChannel"
    const val ANNOUNCEMENT_TOPIC = "Announcement"
    const val LIKE_CHANNEL_ID = "LikeChannel"
    const val LIKE_TOPIC = "Like"
    const val XP_BOOST_CHANNEL_ID = "XpBoostChannel"
    const val XP_BOOST_TOPIC = "XP Boost"

    const val MAX_POST_ADS = 4
    const val MAX_SEARCH_ADS = 2

    /** Compression level for uploading images. */
    const val COMPRESSION_LEVEL = 70

    /** Animation duration for navigation. */
    const val IN_OUT_DURATION = 250
}