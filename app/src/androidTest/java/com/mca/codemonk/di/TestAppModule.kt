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

package com.mca.codemonk.di

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mca.remote.NotificationApi
import com.mca.repository.AuthRepository
import com.mca.repository.HomeRepository
import com.mca.repository.LeaderBoardRepository
import com.mca.repository.NotificationRepository
import com.mca.repository.PostRepository
import com.mca.repository.ProfileRepository
import com.mca.repository.SearchRepository
import com.mca.repository.impl.AuthRepositoryImpl
import com.mca.repository.impl.HomeRepositoryImpl
import com.mca.repository.impl.LeaderBoardRepositoryImpl
import com.mca.repository.impl.NotificationRepositoryImpl
import com.mca.repository.impl.PostRepositoryImpl
import com.mca.repository.impl.ProfileRepositoryImpl
import com.mca.repository.impl.SearchRepositoryImpl
import com.mca.util.constant.Constant.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [AppModule::class])
object TestAppModule {

    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository =
        AuthRepositoryImpl(userRef = FirebaseFirestore.getInstance().collection("users"))

    @Provides
    @Singleton
    fun provideProfileRepository(): ProfileRepository =
        ProfileRepositoryImpl(
            userRef = FirebaseFirestore.getInstance().collection("users"),
            updateRef = FirebaseFirestore.getInstance().collection("update"),
            leaderBoardRef = FirebaseFirestore.getInstance().collection("leaderboard"),
            userStorage = FirebaseStorage.getInstance().getReference("users")
        )

    @Singleton
    @Provides
    fun provideHomeRepository(): HomeRepository =
        HomeRepositoryImpl(
            postDB = FirebaseDatabase.getInstance().getReference("posts"),
            postStorage = FirebaseStorage.getInstance().getReference("posts"),
            userRef = FirebaseFirestore.getInstance().collection("users")
        )

    @Singleton
    @Provides
    fun providePostRepository(): PostRepository =
        PostRepositoryImpl(
            postDB = FirebaseDatabase.getInstance().getReference("posts"),
            userRef = FirebaseFirestore.getInstance().collection("users"),
            postStorage = FirebaseStorage.getInstance().getReference("posts")
        )

    @Singleton
    @Provides
    fun provideSearchRepository(): SearchRepository =
        SearchRepositoryImpl(userRef = FirebaseFirestore.getInstance().collection("users"))

    @Singleton
    @Provides
    fun provideLeaderBoardRepository(): LeaderBoardRepository =
        LeaderBoardRepositoryImpl(
            userRef = FirebaseFirestore.getInstance().collection("users"),
            leaderBoardRef = FirebaseFirestore.getInstance().collection("leaderboard")
        )

    @Singleton
    @Provides
    fun provideNotificationRepository(notificationApi: NotificationApi): NotificationRepository =
        NotificationRepositoryImpl(
            userRef = FirebaseFirestore.getInstance().collection("users"),
            notificationRef = FirebaseDatabase.getInstance().getReference("notifications"),
            notificationApi = notificationApi
        )

    @Singleton
    @Provides
    fun provideNotificationApi(): NotificationApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }
}