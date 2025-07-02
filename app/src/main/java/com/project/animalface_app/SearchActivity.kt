package com.project.animalface_app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchAdapter

    private var itemList = emptyList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressed()
        }

        itemList = listOf("동물상테스트", "테스트 만들기", "상식퀴즈", "성격유형검사")

        searchView = findViewById(R.id.searchView)
        recyclerView = findViewById(R.id.recyclerView)


        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


        adapter = SearchAdapter(itemList) { selectedItem ->
            when (selectedItem) {
                "동물상테스트" -> {
                    val intent = Intent(this, AnimalFaceActivity::class.java)
                    startActivity(intent)
                }
                "테스트 만들기" -> {
                    val intent = Intent(this, CreateGameActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        recyclerView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = if (newText.isNullOrEmpty()) {
                    itemList
                } else {
                    itemList.filter { it.contains(newText, ignoreCase = true) }
                }
                adapter.updateData(filteredList)
                return true
            }
        })
    }
}
