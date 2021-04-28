package com.delixha.consumerapp.helper

import android.database.Cursor
import com.delixha.consumerapp.entity.User
import com.delixha.consumerapp.db.UserContract.UserColums.Companion._ID
import com.delixha.consumerapp.db.UserContract.UserColums.Companion.USERNAME
import com.delixha.consumerapp.db.UserContract.UserColums.Companion.NAME
import com.delixha.consumerapp.db.UserContract.UserColums.Companion.LOCATION
import com.delixha.consumerapp.db.UserContract.UserColums.Companion.REPOSITORY
import com.delixha.consumerapp.db.UserContract.UserColums.Companion.COMPANY
import com.delixha.consumerapp.db.UserContract.UserColums.Companion.AVATAR_URL

object MappingHelper {

    fun mapCursorToArrayList(usersCursor: Cursor?): ArrayList<User> {
        val usersList = ArrayList<User>()

        usersCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(_ID))
                val username = getString(getColumnIndexOrThrow(USERNAME))
                val name = getString(getColumnIndexOrThrow(NAME))
                val location = getString(getColumnIndexOrThrow(LOCATION))
                val repository = getInt(getColumnIndexOrThrow(REPOSITORY))
                val company = getString(getColumnIndexOrThrow(COMPANY))
                val avatar = getString(getColumnIndexOrThrow(AVATAR_URL))
                usersList.add(User(id, username, name, location, repository, company, avatar))
            }
        }
        return usersList
    }
}