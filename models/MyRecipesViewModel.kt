package com.example.assignmenttest3.models


//this model takes parameters and pass them to the MyRecipesRecyclerViewAdapter so this can create the corresponding recycler view
class MyRecipesViewModel(val myRecipeImage: String, val myRecipeName: String, val myRecipeSource: String, val myRecipeTrashIcon:Int, val myRecipeShareIcon:Int) {
}