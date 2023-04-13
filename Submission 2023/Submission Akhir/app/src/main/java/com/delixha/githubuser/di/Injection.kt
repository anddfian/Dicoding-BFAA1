package com.delixha.githubuser.di

import android.content.Context
import com.delixha.githubuser.data.UserRepository
import com.delixha.githubuser.data.local.room.UserDatabase

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val database = UserDatabase.getInstance(context)
        val dao = database.userDao()
        return UserRepository.getInstance(dao)
    }
}