package com.rajneesh.bookhub.adapter

import android.content.Context
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rajneesh.bookhub.R
import com.rajneesh.bookhub.database.BookEntity
import com.squareup.picasso.Picasso

class FavouriteRecyclerAdapter(val context: Context, val bookList: List<BookEntity>): RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteRecyclerAdapter.FavouriteViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_favorite_single_row, parent, false)

        return FavouriteViewHolder(view)

    }
    override fun getItemCount(): Int {
        return bookList.size
    }


    override fun onBindViewHolder(holder: FavouriteRecyclerAdapter.FavouriteViewHolder, position: Int) {

        val book = bookList[position]
        holder.txtBookName.text = book.bookName
        holder.txtBookAuthor.text = book.bookAuthor
        holder.txtBookPrice.text = book.bookPrice
        holder.txtBookRating.text = book.bookRating
        Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover).into(holder.imgBookImage)

    }


    class FavouriteViewHolder(View: View): RecyclerView.ViewHolder(View){
        val txtBookName: TextView = View.findViewById(R.id.txtFavBookTitle)
        val txtBookAuthor: TextView = View.findViewById(R.id.txtFavBookAuthor)
        val txtBookPrice: TextView = View.findViewById(R.id.txtFavBookPrice)
        val txtBookRating: TextView = View.findViewById(R.id.txtFavBookRating)
        val imgBookImage: ImageView = View.findViewById(R.id.imgFavBookImage)
        val llContent: LinearLayout = View.findViewById(R.id.llFavContent)

    }
}

