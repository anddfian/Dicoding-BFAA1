package com.delixha.githubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.delixha.githubuser.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userEntity: UserEntity)

    @Delete
    suspend fun delete(userEntity: UserEntity)

    @Query("SELECT * FROM user")
    fun getFavoriteUser(): LiveData<List<UserEntity>>

    @Query("SELECT * from user WHERE username = :username")
    fun isFavoriteUser(username: String): LiveData<UserEntity>
}