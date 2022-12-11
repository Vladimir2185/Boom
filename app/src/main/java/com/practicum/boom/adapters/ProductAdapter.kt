package com.practicum.boom.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.boom.Product
import com.practicum.boom.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_product_info.view.*

class ProductAdapter(private val context: Context,private  val heightProduct:Int) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    val productList = listOf<Product>()



    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProduct = itemView.ivProduct
        val but = itemView.button
        val cv = itemView.cardView


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_product_info, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {


        holder.cv.layoutParams.height=heightProduct


        Picasso.get() //загрузка изображений с помощью Picasso
            //.load(R.drawable.christmas_trees)
            //.load(file)
            .load(android.R.drawable.btn_star_big_on)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_report_image)
            .into(holder.ivProduct)
        Log.i("test", "maxHeight " + (holder.cv.layoutParams.height))

    }

    //помещает в метод кол-во элентов массива productList т.е. сколько будет в RecyclerView
    override fun getItemCount(): Int {

        return 5//productList.size
    }
}