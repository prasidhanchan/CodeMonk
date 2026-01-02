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

package com.mca.notification.service

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class NotificationHelper {

    companion object {

        /** Firebase token */
        private val token: MutableStateFlow<String?> = MutableStateFlow(null)

        /** Function to set new token */
        fun setToken(newToken: String?) {
            token.update { newToken }
        }

        /** Function to get new token */
        fun getToken(): String? = token.value

        /** Function to clear */
        fun clearToken() = token.update { null }
    }
}