package com.example.assignmenttest3.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmenttest3.R
import com.example.assignmenttest3.models.IngredientsViewModel

//creates an adapter from our IngredientsViewModel
//code adapted from Pushpender007, Android RecyclerView in Kotlin (2021)


class IngredientsViewModelAdapter(private val mList: List<IngredientsViewModel>,
                                  private val listener: OnIngredientClickListener
                                  ) : RecyclerView.Adapter<IngredientsViewModelAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ingredients_card_view_design, parent, false)

        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ingredientsViewModel = mList[position]

        holder.imageView.setImageResource(ingredientsViewModel.image)

        holder.recipeName.text = ingredientsViewModel.text

    }


    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
   inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView),
    OnClickListener{
        val imageView: ImageView = itemView.findViewById(R.id.informationIcon)
        val recipeName: TextView = itemView.findViewById(R.id.recipeName)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            val position:Int = adapterPosition

            if (position != RecyclerView.NO_POSITION){
                listener.onIngredientClick(position)
            }



        }
    }

    interface OnIngredientClickListener{

        fun onIngredientClick(position: Int)
    }
}