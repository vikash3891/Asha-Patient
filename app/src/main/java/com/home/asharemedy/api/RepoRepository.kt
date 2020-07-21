package com.home.asharemedy.api

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RepoRepository {

    // GET repo list
    fun getRepoList(onResult: (isSuccess: Boolean, response: ArrayList<String>?) -> Unit) {

        ApiClient.instance.getRepo("1","10").enqueue(object : Callback<ArrayList<String>> {
            override fun onResponse(call: Call<ArrayList<String>>?, response: Response<ArrayList<String>>?) {
                if (response != null && response.isSuccessful)
                    onResult(true, response.body()!!)
                else
                    onResult(false, null)
            }

            override fun onFailure(call: Call<ArrayList<String>>?, t: Throwable?) {
                onResult(false, null)
            }

        })
    }

    companion object {
        private var INSTANCE: RepoRepository? = null
        fun getInstance() = INSTANCE
                ?: RepoRepository().also {
                    INSTANCE = it
                }
    }
}