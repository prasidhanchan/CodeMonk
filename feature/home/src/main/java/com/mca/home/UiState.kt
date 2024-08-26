package com.mca.home

import com.mca.util.model.Post

data class UiState(
    var profileImage: String = "",
    var posts: List<Post> = emptyList(),
    var loading: Boolean = false
)
