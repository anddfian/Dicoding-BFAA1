package com.delixha.aplikasigithubuser.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.delixha.aplikasigithubuser.data.entity.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DetailViewModel : ViewModel() {
    val listUsers = MutableLiveData<User>()

    fun setUser(username: String?) {
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username"
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                val result = String(responseBody)
                try {
                    val responseObject = JSONObject(result)
                    val user = User(
                    username = responseObject.getString("login"),
                    name = responseObject.getString("name"),
                    location = responseObject.getString("location"),
                    repository = responseObject.getInt("public_repos"),
                    company = responseObject.getString("company"),
                    avatar = responseObject.getString("avatar_url"),
                    )
                    listUsers.postValue(user)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("OnFailure", error.message.toString())
            }
        })
    }

    fun getUser(): LiveData<User> {
        return listUsers
    }
}