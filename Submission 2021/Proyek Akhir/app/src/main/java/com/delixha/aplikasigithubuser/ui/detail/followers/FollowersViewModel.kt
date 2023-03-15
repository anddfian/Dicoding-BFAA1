package com.delixha.aplikasigithubuser.ui.detail.followers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.delixha.aplikasigithubuser.BuildConfig.GITHUB_TOKEN
import com.delixha.aplikasigithubuser.data.entity.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowersViewModel : ViewModel() {
    val listUsers = MutableLiveData<ArrayList<User>>()

    fun setUser(username: String?) {
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/followers"
        client.addHeader("Authorization", GITHUB_TOKEN)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                val listUser = ArrayList<User>()

                val result = String(responseBody)
                try {
                    val responseObject = JSONArray(result)

                    for (i in 0 until responseObject.length()) {
                        val item = responseObject.getJSONObject(i)
                        val userName = item.getString("login")
                        val avatar = item.getString("avatar_url")
                        val user = User()
                        user.username = userName
                        user.avatar = avatar
                        listUser.add(user)
                    }
                    listUsers.postValue(listUser)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("OnFailure", error.message.toString())
            }
        })
    }

    fun getUser(): LiveData<ArrayList<User>> {
        return listUsers
    }
}