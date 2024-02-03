package com.example.assignmenttest3.models

//this model takes parameters and pass them to the ItemsViewModelAdapter so this can create the corresponding recycler view
data class ItemsViewModel(val image: String, val recipeName: String, val recipeSource: String, val recipeCalories: Double ){


}
