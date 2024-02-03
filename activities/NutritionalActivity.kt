package com.example.assignmenttest3.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.assignmenttest3.databinding.ActivityNutritionalBinding


//Nutritional activity displays the nutrition values for each recipe
class NutritionalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNutritionalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNutritionalBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //we will collect all the data from the intent thrown from  Recipe activity on the variables

        val calories = intent.getDoubleExtra("EXTRA_CALORIES", 0.0)
        val roundedCalories = (calories * 10).toInt().toDouble() / 10

        val fats = intent.getDoubleExtra("EXTRA_FATS", 0.0)
        val roundedFats = (fats * 10).toInt().toDouble() / 10

        val carbs = intent.getDoubleExtra("EXTRA_CARBS", 0.0)
        val roundedCarbs = (carbs * 10).toInt().toDouble() / 10

        val fiber = intent.getDoubleExtra("EXTRA_FIBER", 0.0)
        val roundedFiber = (fiber * 10).toInt().toDouble() / 10

        val sugar = intent.getDoubleExtra("EXTRA_SUGAR", 0.0)
        val roundedSugar = (sugar * 10).toInt().toDouble() / 10

        val protein = intent.getDoubleExtra("EXTRA_PROTEIN", 0.0)
        val roundedProtein = (protein * 10).toInt().toDouble() / 10

        val sodium = intent.getDoubleExtra("EXTRA_SODIUM", 0.0)
        val roundedSodium = (sodium * 10).toInt().toDouble() / 10

        val cholesterol = intent.getDoubleExtra("EXTRA_CHOLESTEROL", 0.0)
        val roundedCholesterol = (cholesterol * 10).toInt().toDouble() / 10


        //we use the variables to display the data on our screen

        binding.calories.text = roundedCalories.toString()
        binding.carbs.text = roundedCarbs.toString()
        binding.protein.text = roundedProtein.toString()
        binding.sodium.text = roundedSodium.toString()
        binding.cholesterol.text = roundedCholesterol.toString()
        binding.fats.text = roundedFats.toString()
        binding.sugars.text = roundedSugar.toString()
        binding.fiber.text = roundedFiber.toString()


    }
}