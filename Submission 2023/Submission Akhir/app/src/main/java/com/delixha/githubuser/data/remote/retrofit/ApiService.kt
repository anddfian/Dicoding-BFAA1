package com.delixha.githubuser.data.remote.retrofit

import com.delixha.githubuser.data.remote.response.DetailUserResponse
import com.delixha.githubuser.data.remote.response.GitHubResponse
import com.delixha.githubuser.data.remote.response.ItemsItem
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("users")
    fun getListUser(): Call<List<ItemsItem>>

    @GET("search/users")
    fun findUser(@Query("q") username: String): Call<GitHubResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<DetailUserResponse>

    @GET("users/{username}/{tipe}")
    fun getFollow(@Path("username") username: String, @Path("tipe") tipe: String): Call<List<ItemsItem>>
}