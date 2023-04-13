package com.delixha.githubuser.data

import androidx.lifecycle.LiveData
import com.delixha.githubuser.data.local.entity.UserEntity
import com.delixha.githubuser.data.local.room.UserDao

class UserRepository private constructor(private val userDao: UserDao) {
    suspend fun insert(user: UserEntity) {
        userDao.insert(user)
    }

    suspend fun delete(user: UserEntity) {
        userDao.delete(user)
    }

    fun getFavoriteUser(): LiveData<List<UserEntity>> = userDao.getFavoriteUser()

    fun isFavoriteUser(username: String): LiveData<UserEntity> = userDao.isFavoriteUser(username)

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(userDao: UserDao): UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository(userDao)
        }.also { instance = it }
    }
}