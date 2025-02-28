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

package com.mca.remote

import com.mca.remote.BuildConfig.PROJECT_ID
import com.mca.util.model.PushNotificationToken
import com.mca.util.model.PushNotificationTopic
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface NotificationApi {

    /**
     * Send notification to a topic with [PushNotificationTopic] as the body.
     */
    @POST("$PROJECT_ID/messages:send")
    suspend fun postNotificationToTopic(
        @HeaderMap headers: HashMap<String, String>,
        @Body pushNotification: PushNotificationTopic
    ): Response<ResponseBody>

    /**
     * Send notification to a token with [PushNotificationToken] as the body.
     */
    @POST("$PROJECT_ID/messages:send")
    suspend fun postNotificationToToken(
        @HeaderMap headers: HashMap<String, String>,
        @Body pushNotification: PushNotificationToken
    ): Response<ResponseBody>
}