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

package com.mca.repository.impl

import com.google.common.truth.Truth.assertThat
import com.mca.repository.AuthRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class AuthRepositoryTest {

    @Mock
    private lateinit var authRepository: AuthRepository

    @Before
    fun setUp() {
        authRepository = AuthRepositoryImpl()
    }

    @Test
    fun `login without email throws error`() = runBlocking {
        authRepository.login(
            email = "",
            password = "test123",
            onSuccess = { },
            onError = { error ->
                assertThat(error).isEqualTo("Please enter your email.")
            }
        )
    }

    @Test
    fun `login without password throws error`() = runBlocking {
        authRepository.login(
            email = "test@mail.com",
            password = "",
            onSuccess = { },
            onError = { error ->
                assertThat(error).isEqualTo("Please enter your password.")
            }
        )
    }

    @Test
    fun `forgotPassword without email throws error`() = runBlocking {
        authRepository.forgotPassword(
            email = "",
            onSuccess = { },
            onError = { error ->
                assertThat(error).isEqualTo("Please enter your email.")
            }
        )
    }
}