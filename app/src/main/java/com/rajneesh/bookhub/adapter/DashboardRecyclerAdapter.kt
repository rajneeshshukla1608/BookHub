package com.rajneesh.bookhub.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rajneesh.bookhub.R
import com.rajneesh.bookhub.activity.DescriptionActivity
import com.rajneesh.bookhub.model.Book
import com.squareup.picasso.Picasso

class DashboardRecyclerAdapter(val context : Context,val itemList: ArrayList<Book>) : RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single_row, parent, false)

        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val book = itemList[position]
        holder.txtBookName.text = book.bookName
        holder.txtBookAuthor.text = book.bookAuthor
        holder.txtBookPrice.text = book.bookPrice
        holder.txtBookRating.text = book.bookRating
//        holder.imgBookImage.setImageResource(book.bookImages)
        Picasso.get().load(book.bookImages).error(R.drawable.default_book_cover ).into(holder.imgBookImage)
        //setImageResource is not working


        holder.llcontent.setOnClickListener{
//            Toast.makeText(context, "clicked on ${holder.txtBookName.text}", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, DescriptionActivity::class.java)
             intent.putExtra("book_id", book.bookId)
            context.startActivity(intent)

        }
    }

    class DashboardViewHolder(View : View): RecyclerView.ViewHolder(View){

        val txtBookName: TextView = View.findViewById(R.id.txtBookName)
        val txtBookAuthor: TextView =  View.findViewById(R.id.txtBookAuthor)
        val txtBookPrice: TextView =  View.findViewById(R.id.txtBookPrice)
        val txtBookRating: TextView = View.findViewById(R.id.txtBookRating)
        val imgBookImage: ImageView =  View.findViewById(R.id.imgBookImage)
        val llcontent:  LinearLayout = View.findViewById(R.id.llcontent)


    }

}