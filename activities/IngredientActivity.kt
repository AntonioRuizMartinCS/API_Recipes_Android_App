package com.example.assignmenttest3.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.assignmenttest3.databinding.ActivityIngredientBinding
import com.squareup.picasso.Picasso


class IngredientActivity : AppCompatActivity() {

    private lateinit var binding:ActivityIngredientBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIngredientBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //retrieve all data sent from the RecipeActivity intent

        val ingredientCalories = intent.getStringExtra("EXTRA_INGREDIENT_CALORIES").toString().substringBefore(".")
        val ingredientName = intent.getStringExtra("EXTRA_INGREDIENT_NAME").toString()
        val ingredientImage = intent.getStringExtra("EXTRA_INGREDIENT_IMAGE").toString()
        val ingredientCarbs = intent.getStringExtra("EXTRA_INGREDIENT_CARBS").toString().substringBefore(".")
        val ingredientFats = intent.getStringExtra("EXTRA_INGREDIENT_FATS").toString().substringBefore(".")
        val ingredientSugars = intent.getStringExtra("EXTRA_INGREDIENT_SUGARS").toString().substringBefore(".")
        val ingredientProtein = intent.getStringExtra("EXTRA_INGREDIENT_PROTEIN").toString().substringBefore(".")
        val ingredientSodium = intent.getStringExtra("EXTRA_INGREDIENT_SODIUM").toString().substringBefore(".")
        val ingredientFiber = intent.getStringExtra("EXTRA_INGREDIENT_FIBER").toString().substringBefore(".")
        val ingredientCholesterol = intent.getStringExtra("EXTRA_INGREDIENT_CHOLESTEROL").toString().substringBefore(".")

        //displaying the data on the screen

        binding.calories.text = ingredientCalories
        binding.ingredientName.text = ingredientName
        binding.carbs.text = ingredientCarbs
        binding.fats.text = ingredientFats
        binding.fiber.text = ingredientFiber
        binding.cholesterol.text = ingredientCholesterol
        binding.sodium.text = ingredientSodium
        binding.sugars.text = ingredientSugars
        binding.protein.text = ingredientProtein

        Picasso.get()
            .load(ingredientImage)
            .into(binding.ingredientImage)

    }
}