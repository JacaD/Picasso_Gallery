package com.example.picassogallery.adapters

import android.content.Context
import android.preference.PreferenceManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.picassogallery.R
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

class RecyclerViewAdapter(val context: Context, var entries: ArrayList<String>) : RecyclerView.Adapter<RecyclerViewAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_element, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return entries.count()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindEntry(entries[position], context)
    }

    fun refresh(){
        val urls: ArrayList<String> = ArrayList()
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        val size = sp.getInt("URLCount", 0)
        for (i in 0 until size) {
            val string = sp.getString("URL_$i", null)
            if(string != null) {
                urls.add(string)
            }
        }
        entries = urls
        notifyDataSetChanged()
    }

    fun removeAt(position: Int) {
        entries.removeAt(position)
        val urls: ArrayList<String> = ArrayList()
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        val size = sp.getInt("URLCount", 0)
        for (i in 0 until size) {
            val string = sp.getString("URL_$i", null)
            if(string != null) {
                urls.add(string)
            }
        }
        urls.removeAt(position)
        val spEdit = sp.edit()
        spEdit.putInt("URLCount", urls.size)

        for (i in 0 until urls.size) {
            spEdit.remove("URL_$i")
            spEdit.putString("URL_$i", urls[i])
        }
        spEdit.apply()
        notifyItemRemoved(position)
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val entryImage = itemView.findViewById<ImageView>(R.id.galleryViewItem)
        val title = itemView.findViewById<TextView>(R.id.galleryViewTitle)
        val date = itemView.findViewById<TextView>(R.id.galleryViewDate)
        val tags = itemView.findViewById<TextView>(R.id.galleryViewTags)

        fun bindEntry(entry: String?, context: Context){
            val data = entry?.split(",")
            Picasso.get().load(data!![0]).into(entryImage)
            title.text = data[1]
            date.text = data[2]
            tags.text = "# " + data.subList(3, data.size).joinToString(" #")
//            Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(entryImage)
        }
    }
}