package com.rommelrico.findgithubrepo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.ListView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.view.LayoutInflater
import android.content.Context
import android.view.View
import android.view.ViewGroup

class SearchResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        val searchTerm = intent.getStringExtra("searchTerm")

        // Calling the GitHubRetriever
        val retriever = GitHubRetriever()
        val callback = object: Callback<GitHubSearchResult> {
            override fun onResponse(call: Call<GitHubSearchResult>, response: Response<GitHubSearchResult>) {
                val searchResult = response.body()
                if (searchResult != null) {
                    for (repo in searchResult.items) {
                        println(repo.full_name)
                    }

                    val listView = findViewById<ListView>(R.id.repoListView)
                    val adapter = RepoAdapter(this@SearchResultActivity, android.R.layout.simple_list_item_1, searchResult.items)
                    listView.adapter = adapter
                }
            }

            override fun onFailure(call: Call<GitHubSearchResult>, t: Throwable) {
                println("It's not working")
            }
        }
        retriever.searchRepos(callback, searchTerm!!)
    }
}

class RepoAdapter(context: Context?, resource: Int, objects: List<Repo>?): ArrayAdapter<Repo>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val repoView = inflator.inflate(android.R.layout.simple_list_item_1, parent, false) as TextView

        val repoItem = getItem(position)
        repoView.text = repoItem.full_name

        return repoView
    }
}