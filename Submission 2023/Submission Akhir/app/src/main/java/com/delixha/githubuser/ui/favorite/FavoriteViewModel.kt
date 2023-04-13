package com.delixha.githubuser.ui.favorite

import androidx.lifecycle.ViewModel
import com.delixha.githubuser.data.UserRepository

class FavoriteViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getFavoriteUser() = userRepository.getFavoriteUser()
}