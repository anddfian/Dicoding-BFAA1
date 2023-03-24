package com.delixha.githubuser.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var username: String? = null,
    var avatar: String? = null,
) : Parcelable