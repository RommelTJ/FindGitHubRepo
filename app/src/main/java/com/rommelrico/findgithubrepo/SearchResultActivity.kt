package com.rommelrico.findgithubrepo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class SearchResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        val searchTerm = intent.getStringExtra("searchTerm")
        println("SearchTerm: $searchTerm")
    }
}
