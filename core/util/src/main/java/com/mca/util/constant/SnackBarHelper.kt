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

package com.mca.util.constant

import com.mca.util.warpper.Response
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * Helper class for showing snack bar.
 */
class SnackBarHelper {

    companion object {
        /**
         * State for showing snack bar message.
         */
        var messageState = MutableStateFlow(Response())
            private set

        /**
         * Function to show snack bar message.
         */
        fun showSnackBar(response: Response) = messageState.update { response }
        /**
         * Function to reset snack bar state.
         */
        fun resetMessageState() = messageState.update { Response() }
    }
}