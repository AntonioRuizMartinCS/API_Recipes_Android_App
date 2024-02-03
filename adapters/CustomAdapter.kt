package com.example.assignmenttest3.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmenttest3.models.ItemsViewModel
import com.example.assignmenttest3.R
import com.squareup.picasso.Picasso


//creates an adapter from our ItemsViewModelModel

//code adapted from Pushpender007, Android RecyclerView in Kotlin (2021)


class CustomAdapter(private val mList: List<ItemsViewModel>, private val listener: OnItemClickListener) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {


    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = mList[position]


        //sets the imageView of the card
        Picasso.get()
            .load(itemsViewModel.image)
            .into(holder.imageView)


        // sets the text to the textview from our itemHolder class
        holder.recipeNameHolder.text = itemsViewModel.recipeName
        holder.recipeSourceHolder.text = itemsViewModel.recipeSource
        holder.recipeCaloriesHolder.text = itemsViewModel.recipeCalories.toString().substringBefore(".") + " calories"

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView),
    OnClickListener{
        val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val recipeNameHolder: TextView = itemView.findViewById(R.id.recipeName)
        val recipeSourceHolder: TextView = itemView.findViewById(R.id.recipeSource)
        val recipeCaloriesHolder: TextView = itemView.findViewById(R.id.recipeCalories)

        //sets on click listener to each item view
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }


    interface OnItemClickListener{
        fun onItemClick(position: Int)

    }
}