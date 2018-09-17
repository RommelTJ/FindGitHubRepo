package com.rommelrico.findgithubrepo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        val searchTerm = intent.getStringExtra("searchTerm")

        // Calling the GitHubRetriever
        val retriever = GitHubRetriever()
        val callback = object: Callback<GitHubSearchResult> {
            override fun onResponse(call: Call<GitHubSearchResult>, response: Response<GitHubSearchResult>) {
                println("It's working")
            }

            override fun onFailure(call: Call<GitHubSearchResult>, t: Throwable) {
                println("It's not working")
            }
        }
        retriever.searchRepos(callback, searchTerm!!)
    }
}
