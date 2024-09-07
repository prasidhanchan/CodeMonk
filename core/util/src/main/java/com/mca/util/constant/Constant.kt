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

package com.mca.util.constant

object Constant {

    /** Regex for username ex: pra_sidh_22 */
    val USERNAME_REGEX = Regex("^[a-zA-Z0-9_.]+|[a-zA-Z]+\$")

    /** Regex for deadline ex: 20 Apr 2024 */
    val DEADLINE_REGEX = Regex("\\d{1,2}\\s(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sept|Oct|Nov|Dec)\\s\\d{4}")
}