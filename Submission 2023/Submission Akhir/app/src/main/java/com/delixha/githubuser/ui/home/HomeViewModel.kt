package com.delixha.githubuser.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.delixha.githubuser.data.remote.response.GitHubResponse
import com.delixha.githubuser.data.remote.response.ItemsItem
import com.delixha.githubuser.data.remote.retrofit.ApiConfig
import com.delixha.githubuser.utils.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val _listUser = MutableLiveData<List<ItemsItem>>()
    val listUser: LiveData<List<ItemsItem>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    init {
        getListUser()
    }

    private fun getListUser() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getListUser()
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _isEmpty.value = response.body()?.isNotEmpty() != true
                    _listUser.value = response.body()
                } else {
                    _snackbarText.value = Event("Something went wrong")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                _snackbarText.value = Event("Something went wrong")
            }
        })
    }

    fun findUser(username: String) {
        if (username != "") {
            _isLoading.value = true
            val client = ApiConfig.getApiService().findUser(username)
            client.enqueue(object : Callback<GitHubResponse> {
                override fun onResponse(
                    call: Call<GitHubResponse>,
                    response: Response<GitHubResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _isEmpty.value = response.body()?.totalCount == 0
                        _listUser.value = response.body()?.items
                    } else {
                        _snackbarText.value = Event("Something went wrong")
                    }
                }

                override fun onFailure(call: Call<GitHubResponse>, t: Throwable) {
                    _isLoading.value = false
                    _snackbarText.value = Event("Something went wrong")
                }
            })
        } else {
            getListUser()
        }
    }
}