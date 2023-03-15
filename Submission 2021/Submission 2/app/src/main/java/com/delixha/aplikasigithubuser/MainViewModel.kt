package com.delixha.aplikasigithubuser

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class MainViewModel : ViewModel() {
    val listUsers = MutableLiveData<ArrayList<User>>()

    fun setUser(username: String?) {
        val listUser = ArrayList<User>()
        val client = AsyncHttpClient()
        if (username != "") {
            val url = "https://api.github.com/search/users?q=$username"
            client.addHeader("Authorization", "token ffec0b78f5e6f163812b3daae6ba19fdba29ee69")
            client.addHeader("User-Agent", "request")
            client.get(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                    val result = String(responseBody)
                    try {
                        val responseObject = JSONObject(result)
                        val items = responseObject.getJSONArray("items")
                        for (i in 0 until items.length()) {
                            val item = items.getJSONObject(i)
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
        } else {
            val url = "https://api.github.com/users"
            client.addHeader("Authorization", "token ffec0b78f5e6f163812b3daae6ba19fdba29ee69")
            client.addHeader("User-Agent", "request")
            client.get(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
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
    }

    fun getUser(): LiveData<ArrayList<User>> {
        return listUsers
    }
}