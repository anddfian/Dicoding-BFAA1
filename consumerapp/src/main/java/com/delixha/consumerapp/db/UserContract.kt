package com.delixha.consumerapp.db

import android.net.Uri
import android.provider.BaseColumns

object UserContract {

    const val AUTHORITY = "com.delixha.aplikasigithubuser"
    const val SCHEME = "content"

    class UserColums: BaseColumns {
        companion object {
            const val TABLE_NAME = "user"
            const val _ID = "_id"
            const val USERNAME = "username"
            const val NAME = "name"
            const val LOCATION = "location"
            const val REPOSITORY = "repository"
            const val COMPANY = "company"
            const val AVATAR_URL ="avatar_url"

            // untuk membuat URI content://com.delixha.aplikasigithubuser/user
            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                    .authority(AUTHORITY)
                    .appendPath(TABLE_NAME)
                    .build()
        }
    }
}