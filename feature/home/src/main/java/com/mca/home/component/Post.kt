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

package com.mca.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.ads.nativead.NativeAd
import com.mca.util.constant.Constant.MAX_POST_ADS
import com.mca.util.constant.PostType
import com.mca.util.model.Post
import com.mca.util.model.User

/**
 * Post composable to display user Post or an Announcement.
 */
@Composable
internal fun Post(
    posts: () -> List<Pair<Post, User>>,
    currentUserId: String,
    currentUsername: String,
    currentUserType: String,
    topMembers: List<String>,
    loading: Boolean,
    modifier: Modifier = Modifier,
    state: LazyListState,
    onLikeClick: (postId: String, token: String) -> Unit,
    onUnlikeClick: (postId: String) -> Unit,
    onUsernameClick: (String) -> Unit,
    onEditPostClick: (postId: String) -> Unit,
    onDeleteClick: (postId: String) -> Unit,
    nativeAds: List<NativeAd?>,
    appBar: @Composable () -> Unit = { }
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = state,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item(key = "HomeAppbar") {
            appBar()
        }

        if (posts().isNotEmpty() && !loading) {
            itemsIndexed(items = posts()) { index, (post, user) ->
                when (post.postType) {
                    PostType.PROJECT.name -> {
                        PostCard(
                            post = post,
                            user = user,
                            currentUserId = currentUserId,
                            currentUsername = currentUsername,
                            currentUserType = currentUserType,
                            topMembers = topMembers,
                            onLikeClick = onLikeClick,
                            onUnlikeClick = onUnlikeClick,
                            onUsernameClick = onUsernameClick,
                            onEditPostClick = onEditPostClick,
                            onDeleteClick = onDeleteClick
                        )
                    }

                    PostType.ANNOUNCEMENT.name -> {
                        AnnouncementCard(
                            post = post,
                            user = user,
                            currentUserId = currentUserId,
                            currentUsername = currentUsername,
                            topMembers = topMembers,
                            onUsernameClick = onUsernameClick,
                            onDeleteClick = onDeleteClick,
                            onLikeClick = onLikeClick,
                            onUnlikeCLick = onUnlikeClick
                        )
                    }
                }

                if (index < MAX_POST_ADS && nativeAds.size == MAX_POST_ADS) {
                    PostNativeAd(nativeAd = nativeAds[index])
                }
            }
        } else if (posts().isEmpty() && loading) {
            items(count = 2) {
                PostLoader()
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}