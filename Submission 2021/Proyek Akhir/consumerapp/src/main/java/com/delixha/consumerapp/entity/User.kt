package com.delixha.consumerapp.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: Int = 0,
    var username: String? = null,
    var name: String? = null,
    var location: String? = null,
    var repository: Int? = null,
    var company: String? = null,
    var avatar: String? = null,
) : Parcelable