package com.rajneesh.bookhub.activity

import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.Volley
import com.rajneesh.bookhub.R
import com.rajneesh.bookhub.database.BookDatabase
import com.rajneesh.bookhub.database.BookEntity
import com.rajneesh.bookhub.util.ConnectionManger
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.lang.Exception

class DescriptionActivity : AppCompatActivity() {

    lateinit var txtBookName: TextView
    lateinit var txtBookAuthor :TextView
    lateinit var txtBookPrice: TextView
    lateinit var txtBookRating: TextView
    lateinit var imgBookIamge: ImageView
    lateinit var txtBookDesc: TextView
    lateinit var btnAddToFAv: Button
    lateinit var progressbar: ProgressBar
    lateinit var progressLayout: RelativeLayout
    lateinit var toolbar: Toolbar

    var bookId: String? = "100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        txtBookName = findViewById(R.id.txtBookName)
        txtBookAuthor = findViewById(R.id.txtBookAuthor)
        txtBookPrice = findViewById(R.id.txtBookPrice)
        txtBookRating = findViewById(R.id.txtBookRating)
        imgBookIamge = findViewById(R.id.imgBookImage)
        txtBookDesc = findViewById(R.id.txtbookdesc)
        btnAddToFAv = findViewById(R.id.btnAddFav)
        progressbar = findViewById(R.id.ProgressBar)
        progressbar.visibility = View.VISIBLE
        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Book details"

        if(intent != null){
            bookId = intent.getStringExtra("book_id")
        } else {
            finish()
            Toast.makeText(this@DescriptionActivity,"Some unexpected error occured", Toast.LENGTH_SHORT).show()
        }
        if (bookId == "100"){
            finish()
            Toast.makeText(this@DescriptionActivity,"Some unexpected error occured", Toast.LENGTH_SHORT).show()
        }

        val queue = Volley.newRequestQueue(this@DescriptionActivity)
        val url = "http://13.235.250.119/v1/book/get_book/"
        val jsonParams = JSONObject()
        jsonParams.put("book_id",bookId)

        if (ConnectionManger().checkConnectivity(this@DescriptionActivity)){
            val jsonRequest = object: JsonObjectRequest(Request.Method.POST,url,jsonParams,Response.Listener {
                try {
                    val success = it.getBoolean("success")
                    if (success){
                        val bookJsonObject = it.getJSONObject("book_data")
                        progressLayout.visibility = View.GONE

                        val bookImageUrl = bookJsonObject.getString("image")
                        Picasso.get().load(bookJsonObject.getString("image"))
                            .error(R.drawable.default_book_cover).into(imgBookIamge)
                        txtBookName.text = bookJsonObject.getString("name")
                        txtBookAuthor.text = bookJsonObject.getString("author")
                        txtBookPrice.text = bookJsonObject.getString("price")
                        txtBookRating.text = bookJsonObject.getString("rating")
                        txtBookDesc.text = bookJsonObject.getString("description")

                        val bookEntity = BookEntity(
                            bookId?.toInt() as Int,
                            txtBookName.text.toString(),
                            txtBookAuthor.text.toString(),
                            txtBookPrice.text.toString(),
                            txtBookRating.text.toString(),
                            txtBookDesc.text.toString(),
                            bookImageUrl
                        )

                        val checkFav = DBAsyncTask(applicationContext, bookEntity, 1).execute()
                        val isFav = checkFav.get()

                        if (isFav){
                            btnAddToFAv.text = "Remove from favourite"
                            val favcolor = ContextCompat.getColor(applicationContext, R.color.colorFavourite)
                            btnAddToFAv.setBackgroundColor(favcolor)
                        } else{
                            btnAddToFAv.text = "Add to favourite"
                            val nofavcolor = ContextCompat.getColor(applicationContext,R.color.colorPrimary)
                            btnAddToFAv.setBackgroundColor(nofavcolor)
                        }

                        btnAddToFAv.setOnClickListener {
                            if (!DBAsyncTask(applicationContext, bookEntity, 1).execute().get()){

                                val async = DBAsyncTask(applicationContext, bookEntity, 2).execute()
                                val result = async.get()
                                if (result) {
                                    Toast.makeText(
                                        this@DescriptionActivity,
                                        "Book added to favourites",
                                        Toast.LENGTH_SHORT
                                    ).show()


                                btnAddToFAv.text = "Remove from favourites"
                                val favcolor = ContextCompat.getColor(applicationContext, R.color.colorFavourite)
                                btnAddToFAv.setBackgroundColor(favcolor)
                            } else {
                                Toast.makeText(
                                    this@DescriptionActivity,
                                    "some Error Occureed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {

                            val async = DBAsyncTask(applicationContext, bookEntity, 3).execute()
                                val result = async.get()
                                if (result){
                                    Toast.makeText(
                                        this@DescriptionActivity,
                                        "Book remove from favourites",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    btnAddToFAv.text = "Add to favourites"
                                    val noFavcolor = ContextCompat.getColor(applicationContext, R.color.colorPrimary)
                                    btnAddToFAv.setBackgroundColor(noFavcolor)

                                } else {
                                    Toast.makeText(
                                        this@DescriptionActivity,
                                        "Some error Occured",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                        }

                     }

                    } else {
                        Toast.makeText(this@DescriptionActivity,"Some  Error Occured",Toast.LENGTH_SHORT).show()

                    }

                } catch(e: Exception){
                    Toast.makeText(this@DescriptionActivity,"Some Unexcepted Error Occured",Toast.LENGTH_SHORT).show()

                }
            }, Response.ErrorListener {
                Toast.makeText(this@DescriptionActivity,"Volley Error Occured",Toast.LENGTH_SHORT).show()

            }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "57dfec8c8e251b"
                    return headers
                }
            }
            queue.add(jsonRequest)
        } else{
            val dialog = AlertDialog.Builder(this@DescriptionActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet connection not found")
            dialog.setPositiveButton("Open settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }

            dialog.setNegativeButton("Exit"){ text, listener ->
                ActivityCompat.finishAffinity(this@DescriptionActivity)
            }
            dialog.create()
            dialog.show()
        }

    }


    class DBAsyncTask(val context: Context, val bookEntity: BookEntity, val mode: Int) : AsyncTask<Void, Void, Boolean>() {

        /*
         mode 1 -> check sb if the book is favourite or not
         mode 2 -> save the book into db as favourite
         mode 3 ->  remove the favourite book
         */

        val db = Room.databaseBuilder(context, BookDatabase::class.java,"books-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {

            when(mode) {

                1 -> {

                    //check  if the book is favourite or not
                    val book: BookEntity? = db.bookDao().getBookId(bookEntity.book_id.toString())
                    db.close()
                    return book!= null

                }

                2 -> {

                    //save the book into db as favourite
                    db.bookDao().insertBook(bookEntity)
                    db.close()
                    return true

                }

                3 -> {

                    //remove the favourite book
                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true


                }

            }


            return false
        }
    }

}