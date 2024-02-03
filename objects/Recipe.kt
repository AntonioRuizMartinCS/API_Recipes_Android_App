package com.example.assignmenttest3.objects

import java.io.Serializable

//Recipe class - This class represents a recipe object

data class Recipe(

    var id:Int,
    var name:String,
    var image:String,
    var source:String,
    var url:String,
    var dietLabels:ArrayList<String>,
    var healthLabels:ArrayList<String>,
    var ingredientLines:ArrayList<String>,
    var ingredients:ArrayList<RecipeIngredient>,
    var nutrients: RecipeNutrients


    //serializable will make the class convertible into bytes and be stored and passed easier
): Serializable{

    //creating a constructor for our object
    constructor(recipeID: Int, recipeName: String, recipeSource: String, recipeURL: String, recipeImage:String) : this(

        id = recipeID,
        name = recipeName,
        image = recipeImage,
        source = recipeSource,
        url = recipeURL,
        dietLabels = ArrayList(),
        healthLabels = ArrayList(),
        ingredientLines = ArrayList(),
        ingredients = ArrayList(),
        nutrients = RecipeNutrients(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

    ) {

    }


}