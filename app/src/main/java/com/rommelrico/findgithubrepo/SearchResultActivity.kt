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
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso

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
                    listView.setOnItemClickListener { parent, view, position, id ->
                        val selectedRepo = searchResult.items[position]
                        // Open URL in browser.
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(selectedRepo.html_url))
                        startActivity(browserIntent)
                    }

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
        val repoView = inflator.inflate(R.layout.repo_list_layout, parent, false)

        val textView = repoView.findViewById<TextView>(R.id.repoTextView)
        val imageView = repoView.findViewById<ImageView>(R.id.repoImageView)

        val repoItem = getItem(position)
        Picasso.get().load(Uri.parse(repoItem.owner.avatar_url)).into(imageView)
        textView.text = repoItem.full_name

        return repoView
    }
}