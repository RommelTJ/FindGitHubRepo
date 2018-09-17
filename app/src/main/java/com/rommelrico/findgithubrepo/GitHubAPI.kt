package com.rommelrico.findgithubrepo

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubService {

    @GET("search/repositories?")
    fun searchRepos(@Query("q") q: String): Call<GitHubSearchResult>

}

class GitHubSearchResult(val items: List<Repo>)
class Repo(val full_name: String, val owner: GitHubUser)
class GitHubUser(val avatar_url: String)
