package com.example.assignmenttest3.objects

import java.io.Serializable

//RecipeIngredient class - This class represents a recipe object
data class RecipeIngredient (

    var ingredientFullLine:String,
    var ingredientName:String,
    var measure:String,
    var quantity:Double,
    var foodId:String,
    var ingredientImage:String
    //serializable will make the class convertible into bytes and be stored and passed easier
    ): Serializable