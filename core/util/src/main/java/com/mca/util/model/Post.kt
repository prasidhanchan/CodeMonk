package com.mca.util.model

data class Post(
    val userId: String,
    val username: String,
    val userImage: String,
    val currentProject: String,
    val teamMembers: List<String>,
    val projectProgress: Float,
    val deadline: String,
    val likes: List<String>,
    val timeStamp: Long
)
