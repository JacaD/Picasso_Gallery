package com.example.picassogallery

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SimpleAdapter
import com.example.picassogallery.adapters.RecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var adapter: RecyclerViewAdapter

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val addImage: MenuItem? = menu?.findItem(R.id.addImage)
        val addImageIntent = Intent(this, AddImageActivity::class.java)
        addImage?.intent = addImageIntent
        return true
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var urls: ArrayList<String> = ArrayList()
        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        val size = sp.getInt("URLCount", 0)
        for (i in 0 until size) {
            val string = sp.getString("URL_$i", null)
            if(string != null) {
                urls.add(string)
            }
        }
        urls.reverse()
        adapter = RecyclerViewAdapter(this, urls)
        galleryView.adapter = adapter

        val layoutManager = LinearLayoutManager(this)
        galleryView.layoutManager = layoutManager
        galleryView.setHasFixedSize(true)
        val dividerItemDecoration = DividerItemDecoration(
            galleryView.context,
            layoutManager.orientation
        )
        galleryView.addItemDecoration(dividerItemDecoration)

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = galleryView.adapter as RecyclerViewAdapter
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(galleryView)

    }

    override fun onResume() {
        super.onResume()
        adapter.refresh()
    }
}
