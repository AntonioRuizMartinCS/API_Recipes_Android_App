package com.example.assignmenttest3.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmenttest3.R
import com.example.assignmenttest3.activities.MyRecipesActivity
import com.example.assignmenttest3.models.MyRecipesViewModel
import com.squareup.picasso.Picasso

//creates an adapter from our ItemsViewModelModel

//code adapted from Pushpender007, Android RecyclerView in Kotlin (2021)

class MyRecipesRecyclerViewAdapter(private val mList: List<MyRecipesViewModel>,
                                   private val listener: OnItemClickListener,
                                   private val onDelete: OnDeleteClickListener

                                   )
    : RecyclerView.Adapter<MyRecipesRecyclerViewAdapter.ViewHolder>() {


    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the designed card view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_recipes_card_view_design, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val myRecipesViewModel = mList[position]


        Picasso.get()
            .load(myRecipesViewModel.myRecipeImage)
            .into(holder.myRecipesImageView)

        // sets the text to the textview from our itemHolder class
        holder.myRecipesNameHolder.text = myRecipesViewModel.myRecipeName
        holder.myRecipesSourceHolder.text = myRecipesViewModel.myRecipeSource
        holder.myRecipesDeleteButton.setImageResource(myRecipesViewModel.myRecipeTrashIcon)
        holder.myRecipesShareIcon.setImageResource(myRecipesViewModel.myRecipeShareIcon)

        //sets on click listener to the trash can icon
        holder.myRecipesDeleteButton.setOnClickListener {
            onDelete.onDeleteClick(position)
        }

        //sets on click listener to the share icon
        holder.myRecipesShareIcon.setOnClickListener{
            onDelete.onShareClick(position)
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }


    // Holds the views for adding it to image and text
    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView),
        OnClickListener, OnDeleteClickListener{

        val myRecipesImageView: ImageView = itemView.findViewById(R.id.myRecipesImageView)
        val myRecipesNameHolder: TextView = itemView.findViewById(R.id.myRecipesName )
        val myRecipesSourceHolder: TextView = itemView.findViewById(R.id.myRecipesSource)
        val myRecipesDeleteButton: ImageView = itemView.findViewById(R.id.trashCan)
        val myRecipesShareIcon: ImageView = itemView.findViewById(R.id.shareMyRecipe)


        init {
            myRecipesDeleteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onDelete.onDeleteClick(position)
                }
            }

            myRecipesShareIcon.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onShareClick(position)
                }
            }

            itemView.setOnClickListener(this)
        }


        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)

            }
        }

        override fun onDeleteClick(position: Int) {

            if (position != RecyclerView.NO_POSITION) {
                onDelete.onDeleteClick(position)
            }

        }

        override fun onShareClick(position: Int) {


            if (position != RecyclerView.NO_POSITION) {
                onShareClick(position)
            }
        }
    }


    interface OnItemClickListener{
        fun onItemClick(position: Int)

    }

    interface OnDeleteClickListener{
        fun onDeleteClick(position: Int)
        fun onShareClick(position: Int)
    }
}