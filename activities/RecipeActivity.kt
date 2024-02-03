package com.example.assignmenttest3.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.assignmenttest3.*
import com.example.assignmenttest3.adapters.IngredientsViewModelAdapter
import com.example.assignmenttest3.databinding.ActivityRecipeBinding
import com.example.assignmenttest3.models.IngredientsViewModel
import com.example.assignmenttest3.objects.*
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


//Activity that shows information for each individual activity
class RecipeActivity : AppCompatActivity(), IngredientsViewModelAdapter.OnIngredientClickListener {


    //instantiating all variables we need to locate in the global scope
    private lateinit var binding: ActivityRecipeBinding

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var mediaPlayer: MediaPlayer
    private lateinit var name:String
    private lateinit var image:String
    private lateinit var source:String
    private lateinit var url:String
    private lateinit var ingredients:ArrayList<RecipeIngredient>
    private lateinit var nutrients: RecipeNutrients
    private lateinit var recipe: Recipe



    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {

        //getting all the variables we need from the main activity intent

        name = intent.getStringExtra("EXTRA_NAME").toString()
        image = intent.getStringExtra("EXTRA_IMAGE").toString()
        source = intent.getStringExtra("EXTRA_SOURCE").toString()
        url = intent.getStringExtra("EXTRA_URL").toString()
        ingredients = intent.getSerializableExtra("EXTRA_INGREDIENTS") as ArrayList<RecipeIngredient>
        nutrients = intent.getSerializableExtra("EXTRA_NUTRIENTS") as RecipeNutrients
        recipe = intent.getSerializableExtra("EXTRA_RECIPE")as Recipe

        super.onCreate(savedInstanceState)
        binding = ActivityRecipeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //creating the instruction buttons
        val instructionsButton:Button = findViewById(R.id.instructionsButton)
        instructionsButton.setOnClickListener{
            loadInstructions()
        }

        //using the Picasso library to load our recipe image into our image view

        Picasso.get()
            .load(image)
            .into(binding.recipeImageView)


        //displaying recipe name and source on our screen
        binding.recipeName.text = name
        binding.recipeSourceView.text = source

        //setting the nutritional button
        binding.nutritionButton.setOnClickListener{
            getNutritionalInfo()
        }

        //creates drawer menu and feedback buttons inside this
        createDrawerMenu()
        createFeedbackButton()


        //building the recyclerView
        //code adapted from Pushpender007, Android RecyclerView in Kotlin (2021)


        val recyclerview = findViewById<RecyclerView>(R.id.ingredientsRecyclerView)

        recyclerview.layoutManager = LinearLayoutManager(this)

        val data = ArrayList<IngredientsViewModel>()

        for (i in 0 until ingredients.size) {
            data.add(IngredientsViewModel(R.drawable.ic_information, ingredients[i].ingredientFullLine))
        }

        val adapter = IngredientsViewModelAdapter(data, this)

        recyclerview.adapter = adapter

    }

    //inflates chosen menu bar for our activity
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.recipe_bar_menu, menu)
        return true
    }

    //sets an on click listener on each menu bar button
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)){
            return true
        }

        //creates new DatabaseHelper object
        val databaseHelper = DataBaseHelper(this)
        //returns the number of recipes in our local database
        val dataBaseSavedRecipesCount = databaseHelper.recipeCount


        val success: Boolean

        when(item.itemId){

            //when share icon is pressed
            R.id.miShare-> {

                //code adapted from official Android Documentation
                //creates a new intent
                val sendIntent: Intent = Intent().apply {

                    //sets the properties for the share intent
                    action = Intent.ACTION_SEND
                    //custom the intent with our own data
                    putExtra(Intent.EXTRA_SUBJECT, "Check out this cool recipe!")
                    putExtra(Intent.EXTRA_TEXT, url)
                    type = "text/plain"
                }

                //starts the share intent that contains the previous send intent
                val shareIntent = Intent.createChooser(sendIntent, "Recipe share")
                startActivity(shareIntent)

            }

            //when favorite icon is pressed
            R.id.miFavorite ->{

                //check if the recipe is already in our favorites recipes
                if (databaseHelper.checkForName(recipe.name) == false){

                    //when the recipes in our db are 5 openDialog function is called
                    if (dataBaseSavedRecipesCount==5){

                        openDialog()
                        //adds the recipe to the db
                        databaseHelper.addOne(recipe)

                    }else{

                        //if the addition has succeeded a toast is made to notify the user
                        success = databaseHelper.addOne(recipe)
                        if (success){
                            Toast.makeText(this, "Recipe added to your saved recipes", Toast.LENGTH_SHORT).show()
                        }
                    }

                    //if recipe is already in saved recipes the user is notified
                }else   {
                    Toast.makeText(this, "Recipe is already on your saved recipes", Toast.LENGTH_SHORT).show()
                }
            }

        }
        return super.onOptionsItemSelected(item)
    }

    //this function will throw an intent to start the nutritional activity screen
    private fun getNutritionalInfo(){

        Intent(this, NutritionalActivity::class.java).also {

            //the intent contains all the necessary data

            it.putExtra("EXTRA_CALORIES", nutrients.calories)
            it.putExtra("EXTRA_FATS", nutrients.fats)
            it.putExtra("EXTRA_CARBS", nutrients.carbs)
            it.putExtra("EXTRA_SUGAR", nutrients.sugar)
            it.putExtra("EXTRA_PROTEIN", nutrients.protein )
            it.putExtra("EXTRA_SODIUM", nutrients.sodium)
            it.putExtra("EXTRA_FIBER", nutrients.fiber)
            it.putExtra("EXTRA_CHOLESTEROL", nutrients.cholesterol)

            startActivity(it)

        }
    }

    //this function will throw the intent to start the web view activity containing the website screen for the recipe
    private fun loadInstructions(){

        Intent(this, WebViewActivity::class.java).also {


            it.putExtra("EXTRA_URL", url)

            startActivity(it)

        }

    }

    //when we click on each ingredient of the recipe we call the getIngredientInfo function below
    override fun onIngredientClick(position: Int) {

        getIngredientInfo(position)

    }


    //inside this function we make an POST request to a different API, Food Database API, to get each ingredient information
    private fun getIngredientInfo(position:Int){

        //we use a similar method implemented in the previous API requests

        val url = "https://api.edamam.com/api/food-database/v2/nutrients?app_id=74188328&app_key=0129a9720e01344e16807f3a4e39dc11"

        val queue = Volley.newRequestQueue(this)

            val jsonObject = JSONObject()
            val ingredientsArray = JSONArray()
            val ingredientObject = JSONObject()

            //the difference now is that we need to put the necessary data in the request and post it

            ingredientObject.put("quantity", ingredients[position].quantity)
            ingredientObject.put("measureURI", ingredients[position].measure)
            ingredientObject.put("qualifiers", JSONArray().put(""))
            ingredientObject.put("foodId", ingredients[position].foodId)


            ingredientsArray.put(ingredientObject)

            jsonObject.put("ingredients", ingredientsArray)

            val requestBody = jsonObject.toString()

            //we make the post request here

            //we will run it on a background thread as well
            val backgroundThread = Thread{

                val request = object : StringRequest(Method.POST, url, Response.Listener { response ->
                    // handle success response here

                    parseJson(response.toString(), position)


                }, Response.ErrorListener { error ->

                    error.printStackTrace()
                    Toast.makeText(this, "ups, that didn't go well...", Toast.LENGTH_SHORT).show()


                }) {
                    override fun getBodyContentType(): String {
                        return "application/json"
                    }

                    override fun getBody(): ByteArray {
                        return requestBody.toByteArray()
                    }
                }

                queue.add(request)


            }
        //starting the background thread
        backgroundThread.start()


    }


    //handles the result of our request
    private fun parseJson(jsonData: String, position: Int) {



        val jsonObject = JSONObject(jsonData)
        val totalNutrientsObject = jsonObject.getJSONObject("totalNutrients")

        //gets all the variables that we need to display in our ingredients activity

        val ingredientCalories =  jsonObject.getString("calories").toString()
        val ingredientTotalWeight = jsonObject.getString("totalWeight").toString()
        val ingredientFats = totalNutrientsObject.getJSONObject("FAT").getString("quantity")
        val ingredientCarbs = totalNutrientsObject.getJSONObject("CHOCDF").getString("quantity")
        val ingredientSugars = totalNutrientsObject.getJSONObject("SUGAR").getString("quantity")
        val ingredientFiber = totalNutrientsObject.getJSONObject("FIBTG").getString("quantity")
        val ingredientSodium = totalNutrientsObject.getJSONObject("NA").getString("quantity")
        val ingredientCholesterol = totalNutrientsObject.getJSONObject("CHOLE").getString("quantity")
        val ingredientProtein = totalNutrientsObject.getJSONObject("PROCNT").getString("quantity")


        //throw the intent that starts ingredients activity with our ingredient information screen

        Intent(this, IngredientActivity::class.java).also {

            it.putExtra("EXTRA_INGREDIENT_CALORIES", ingredientCalories)
            it.putExtra("EXTRA_INGREDIENT_WEIGHT", ingredientTotalWeight)
            it.putExtra("EXTRA_INGREDIENT_NAME", ingredients[position].ingredientName)
            it.putExtra("EXTRA_INGREDIENT_IMAGE", ingredients[position].ingredientImage)
            it.putExtra("EXTRA_INGREDIENT_FATS", ingredientFats)
            it.putExtra("EXTRA_INGREDIENT_CARBS", ingredientCarbs)
            it.putExtra("EXTRA_INGREDIENT_SUGARS", ingredientSugars)
            it.putExtra("EXTRA_INGREDIENT_FIBER", ingredientFiber)
            it.putExtra("EXTRA_INGREDIENT_SODIUM", ingredientSodium)
            it.putExtra("EXTRA_INGREDIENT_CHOLESTEROL", ingredientCholesterol)
            it.putExtra("EXTRA_INGREDIENT_PROTEIN", ingredientProtein)


            //error handles starting the new intent
            try {

                startActivity(it)

            }catch (e: JSONException){

                e.printStackTrace()
                Toast.makeText(this, "ups, that didn't go well...", Toast.LENGTH_SHORT).show()
            }

        }

    }

    //creates drawer menu as seen in main activity
    //code adapted from Foxandroid (2022)
    private fun createDrawerMenu(){

        val drawerLayout: DrawerLayout = binding.drawerLayout

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navView.setNavigationItemSelectedListener { it ->

            when(it.itemId){
                R.id.myRecipes -> {
                    Intent(this, MyRecipesActivity::class.java).also {
                        startActivity(it)
                    }

                }
                R.id.myHome -> Intent(this, MainActivity::class.java).also {
                    startActivity(it)
                }

                R.id.preferences -> Intent(this, SettingsActivity::class.java). also {
                    startActivity(it)
                }

            }
            true
        }

    }

    private fun createFeedbackButton(){

        val goodFeedBackButton = binding.thumbsUp
        val badFeedBackButton = binding.thumbsDown

        goodFeedBackButton.setOnClickListener{
            mediaPlayer = MediaPlayer.create(this, R.raw.siuuuu)
            mediaPlayer.start()
        }

        badFeedBackButton.setOnClickListener{
            mediaPlayer = MediaPlayer.create(this, R.raw.hellodarkness)
            mediaPlayer.start()
        }
    }


    //creates and opens the dialog used when user hits 5 recipes in the db
    private fun openDialog(){


        val exampleDialog = ExampleDialog()

        exampleDialog.show(supportFragmentManager, "example dialog")

    }

}


































