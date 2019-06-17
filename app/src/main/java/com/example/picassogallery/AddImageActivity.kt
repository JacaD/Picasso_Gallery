package com.example.picassogallery

import android.app.DatePickerDialog
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_new_image.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.app.Activity
import android.net.Uri
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception


class AddImageActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_image)
//        fun View.hideKeyboard() {
//            val inputMethodManager = context!!.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as? InputMethodManager
//            inputMethodManager?.hideSoftInputFromWindow(this.windowToken, 0)
//        }

        val date: TextView = findViewById(R.id.datePicker)
        val url: TextView = findViewById(R.id.inputURL)
        val tags: TextView = findViewById(R.id.titleView)
        val title: TextView = findViewById(R.id.tagsView)
//        url.setOn{it, it.hideKeyboard()}
        url.setOnFocusChangeListener { v: View, hasFocus: Boolean ->  fun onFocusChange(v: View, hasFocus: Boolean){}
            if(!hasFocus){val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)}
        }
        tags.setOnFocusChangeListener { v: View, hasFocus: Boolean ->  fun onFocusChange(v: View, hasFocus: Boolean){}
            if(!hasFocus){val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)}
        }
        title.setOnFocusChangeListener { v: View, hasFocus: Boolean ->  fun onFocusChange(v: View, hasFocus: Boolean){}
            if(!hasFocus){val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)}
        }

        date.text = SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis())

        var cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd.MM.yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            date.text = sdf.format(cal.time)

        }
        date.inputType = InputType.TYPE_NULL
        date.showSoftInputOnFocus = false
        date.setOnFocusChangeListener { v: View, hasFocus: Boolean ->  fun onFocusChange(v: View, hasFocus: Boolean){}
            if(hasFocus) DatePickerDialog(this@AddImageActivity, dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)).show()}

        date.setOnClickListener {
            DatePickerDialog(this@AddImageActivity, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }
    fun addImage(v: View) {
        val url: String = inputURL.text.toString()
        val title: String = titleView.text.toString()
        val date: String = datePicker.text.toString()
        val tags: String = tagsView.text.toString()

        if(url == "") {Toast.makeText(applicationContext, "Empty URL", Toast.LENGTH_SHORT).show(); return}
        val image = ImageView(this)
        Picasso.get().load(url).into(image, object: Callback {
            override fun onSuccess() {
                val urls: ArrayList<String?> = ArrayList()
                val sp = PreferenceManager.getDefaultSharedPreferences(this@AddImageActivity)
                val size = sp.getInt("URLCount", 0)
                for (i in 0 until size) {
                    urls.add(sp.getString("URL_$i", null))
                }

                if(!urls.contains(url)) {
                    urls.add("$url,$title,$date,$tags")
                }

                val spEdit = sp.edit()
                spEdit.putInt("URLCount", urls.size)

                for (i in 0 until urls.size) {
                    spEdit.remove("URL_$i")
                    spEdit.putString("URL_$i", urls[i])
                }
                spEdit.apply()
                Toast.makeText(applicationContext, "Image Added", Toast.LENGTH_SHORT).show()
            }

            override fun onError(e: Exception?) {
                Toast.makeText(applicationContext, "Failed to load img", Toast.LENGTH_SHORT).show()
            }
        })


    }
}