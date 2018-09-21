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
import android.support.design.widget.Snackbar
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso

class SearchResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        val searchTerm = intent.getStringExtra("searchTerm")

        val retriever = GitHubRetriever()
        if (searchTerm != null) {
            // CASE: Searching GitHub Projects
            val callback = object: Callback<GitHubSearchResult> {
                override fun onResponse(call: Call<GitHubSearchResult>, response: Response<GitHubSearchResult>) {
                    val searchResult = response.body()
                    val statusCode = response.code()
                    if (statusCode == 404) {
                        println("Handle 404")
                    } else if (searchResult != null) {
                        listProjects(projects = searchResult)
                    }
                }

                override fun onFailure(call: Call<GitHubSearchResult>, t: Throwable) {
                    // Handle Failures
                }
            }
            retriever.searchRepos(callback, searchTerm)
        } else {
            // CASE: Searching GitHub Username Repos.
            val username = intent.getStringExtra("userSearchTerm")

            val callback = object: Callback<List<Repo>> {
                override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                    val searchResult = response.body()
                    val statusCode = response.code()
                    if (statusCode == 404) {
                        val searchView = findViewById<View>(R.id.searchActivity)
                        Snackbar.make(searchView, "User Not Found :(", Snackbar.LENGTH_LONG).show()
                    } else if (searchResult != null) {
                        listRepos(repos = searchResult)
                    }
                }

                override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                    // Handle Failures
                }
            }

            retriever.userRepos(callback, username)
        }
    }

    fun listRepos(repos: List<Repo>) {
        val listView = findViewById<ListView>(R.id.repoListView)
        listView.setOnItemClickListener { parent, view, position, id ->
            val selectedRepo = repos[position]
            // Open URL in browser.
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(selectedRepo.html_url))
            startActivity(browserIntent)
        }

        val adapter = RepoAdapter(this@SearchResultActivity, android.R.layout.simple_list_item_1, repos)
        listView.adapter = adapter
    }

    fun listProjects(projects: GitHubSearchResult) {
        val listView = findViewById<ListView>(R.id.repoListView)
        listView.setOnItemClickListener { parent, view, position, id ->
            val selectedRepo = projects.items[position]
            // Open URL in browser.
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(selectedRepo.html_url))
            startActivity(browserIntent)
        }

        val adapter = RepoAdapter(this@SearchResultActivity, android.R.layout.simple_list_item_1, projects.items)
        listView.adapter = adapter
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