package com.example.assignmenttest3.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.drawerlayout.widget.DrawerLayout
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.assignmenttest3.*
import com.example.assignmenttest3.adapters.CustomAdapter
import com.example.assignmenttest3.databinding.ActivityMainBinding
import com.example.assignmenttest3.models.ItemsViewModel
import com.example.assignmenttest3.objects.Recipe
import com.example.assignmenttest3.objects.RecipeIngredient
import com.example.assignmenttest3.objects.RecipeNutrients
import org.json.JSONException
import org.json.JSONObject

//Main activity - Main screen
class MainActivity : AppCompatActivity(), CustomAdapter.OnItemClickListener {

    //instantiating the variables that we need in the global scope here

    lateinit var mediaPlayer: MediaPlayer
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityMainBinding
    private var  recipes: ArrayList<Recipe> = arrayListOf()
    var urlRandomRecipe = "https://api.edamam.com/api/recipes/v2?type=public&q=bread&app_id=254d3b24&app_key=22a50f4621cac2f07d1cdc979bbfd3f5&random=true&field=uri&field=label&field=image&field=source&field=url&field=dietLabels&field=healthLabels&field=ingredientLines&field=ingredients&field=totalNutrients"
    var dietTypeFilter = ""
    var healthTypeFilter = ""





    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        //We will use this bindng configuration for each activity in our app.
        //This code sets the content view of the activity to the root view of the inflated layout file
        // which allows the user interface defined in the XML file to be displayed on the screen


        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Calling the functions that we need to build our main activity screen when it is created

        createDrawerMenu()
        createFeedbackButton()
        applyPreferences()
        buildSpinners()

        //Sets an onclickListener on the search button to call for the getRecipes function

        var searchButton = findViewById<Button>(R.id.searchButton)
        searchButton.setOnClickListener{
            getRecipes(searchButton)
        }




    }

    //instantiating and inflating the chosen menu bar for this screen or activity
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        return true
    }

    //setting an onClickListener for the burger icon of our menu bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    //creates de feedback buttons located inside the drawer menu
    private fun createFeedbackButton(){

        val goodFeedBackButton = binding.thumbsUp
        val badFeedBackButton = binding.thumbsDown

    //setting an onClickListener on the buttons and using our media player instantiated on the global scope to play audio

        goodFeedBackButton.setOnClickListener{
            mediaPlayer = MediaPlayer.create(this, R.raw.siuuuu)
            mediaPlayer.start()
        }

        badFeedBackButton.setOnClickListener{
            mediaPlayer = MediaPlayer.create(this, R.raw.hellodarkness)
            mediaPlayer.start()
        }
    }



    //builds the spinners that we will use as filters for our API request

    //code acapted from Philipp Lackner - Spinner - Android Fundamentals (2020)

    private fun buildSpinners(){

        //select our spinners

        val dietTypeSpinner: Spinner = binding.spinnerDietType
        val healthLabelSpinner: Spinner = binding.spinnerHealthLabel

        //create an adapter
        ArrayAdapter.createFromResource(
            this,
            R.array.healthLabels,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            healthLabelSpinner.adapter = adapter

        }

        //sets an on click listener on each item of healthLabelSpinner

        healthLabelSpinner.onItemSelectedListener = object :

            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {


                //click returns a string with the health type we selected, to use in our API request url
                healthTypeFilter = healthLabelSpinner.selectedItem.toString().lowercase()

            }

            //we dont need to implement on noching selected
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }


        //same procces with the second spinner to get our diet type

        ArrayAdapter.createFromResource(
            this,
            R.array.dietTypes,
            android.R.layout.simple_spinner_item
        ).also { adapter ->

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            dietTypeSpinner.adapter = adapter

        }

        dietTypeSpinner.onItemSelectedListener = object :

            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                dietTypeFilter = dietTypeSpinner.selectedItem.toString().lowercase()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }



    //apply the preferences from our shared preferences
    private fun applyPreferences(){

        //gets our set shared preferences
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        //instantiating each preference

        val showWelcomeMessage = prefs.getBoolean("show_welcome_message", false)
        val activateDarkMode = prefs.getBoolean("activate_dark_mode", false)
        val userName = prefs.getString("user_name", "User")
        val funMode = prefs.getBoolean("fun_mode", false)

        //using our preference we change our welcome message
        binding.welcomeMessage.text = "Welcome $userName"


        // sets the action for our dark mode preference switch
        if (activateDarkMode == true){
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
        }

        // sets the action for switch to hide or display our welcome message
        if (showWelcomeMessage == true){
            findViewById<View>(R.id.welcomeMessage).visibility = View.VISIBLE

        }else{
            findViewById<View>(R.id.welcomeMessage).visibility = View.GONE
        }

        //sets the action for fun mode preference

        if (funMode){

            //hide or display main screen elements in function of the mode

            findViewById<View>(R.id.searchButton).visibility = View.GONE
            findViewById<View>(R.id.spinnerHealthLabel).visibility = View.GONE
            findViewById<View>(R.id.spinnerDietType).visibility = View.GONE
            findViewById<View>(R.id.editTextRecipeSearch).visibility = View.GONE
            findViewById<View>(R.id.videoView).visibility = View.VISIBLE

            //play the funny video of the main screen when app in fun mode
            playFunVideo()


            //changes the on click listener of our random button depending if app in fun mode or not

            val randomButton = findViewById<Button>(R.id.randomButton)

            randomButton.setOnClickListener{
                    getRandomRecipeFunMode(randomButton)

            }

        }else{
            val randomButton = findViewById<Button>(R.id.randomButton)
            findViewById<View>(R.id.videoView).visibility = View.GONE

            randomButton.setOnClickListener{
                    getRandomRecipe(randomButton)
            }
        }

    }

    //code adapted from Foxandroid, Navigation Drawer with Fragments using Kotlin || Android Studio Tutorial - 2021 (2022)

    private fun createDrawerMenu(){

        //instantiates our drawer layout that we set in our xml
        val drawerLayout:DrawerLayout = binding.drawerLayout

        //setting the drawer layout toggle action
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //sets on click listener for each item of the drawer menu
        binding.navView.setNavigationItemSelectedListener {

            when(it.itemId){

                //closes menu because we are already in main activity (home)
                R.id.myHome -> drawerLayout.closeDrawers()

                //sets intent that starts my recipes activity
                R.id.myRecipes -> {
                    Intent(this, MyRecipesActivity::class.java).also {

                        startActivity(it)
                    }
                }

                //sets intent that starts settings activity (preferences)
                R.id.preferences -> {
                    Intent(this, SettingsActivity::class.java).also {
                        startActivity(it)
                    }
                }
            }
            true
        }

    }




    private fun getRecipes(view: View) {

        //instantiate our hiden progress bar and displays it when starting the api call
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = ProgressBar.VISIBLE

        //runs the api call on background thread. Threads are only useful for non instant processes, so we only need them in our more durable processes: API requests
        val backgroundThread = Thread{

            //url for API call, implements filters from spinners and search bar
            val url = "https://api.edamam.com/api/recipes/v2?type=public&${searchString()}&app_id=254d3b24&app_key=22a50f4621cac2f07d1cdc979bbfd3f5${dietTypeFilter()}${healthLabelFilter()}&random=false&field=uri&field=label&field=image&field=source&field=url&field=dietLabels&field=healthLabels&field=ingredientLines&field=ingredients&field=totalNutrients"

            //code adapted from Coding in Flow, How to Parse a Json Using Volley - SIMPLE GET REQUEST - Android Studio Tutorial (2018)
            //https://www.youtube.com/watch?v=y2xtLqP8dSQ

            //creates variable jsonArray that will store our request with the information to make the API call
            val jsonArray = JsonObjectRequest(Request.Method.GET, url, null, { response ->
                try {

                    parseJson(response.toString())

                    //hides the progress bar when the process has finished
                    progressBar.visibility = ProgressBar.INVISIBLE

                } catch (e: JSONException){

                    e.printStackTrace()

                    //lets the user know if the request did not succeed
                    Toast.makeText(view.context, "ups, that didn't go well...", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = ProgressBar.INVISIBLE
                }

            }
            )
            { }

            //instantiate a new request queue for our volley library, that handles our network requests
            val requestQueue: RequestQueue = Volley.newRequestQueue(view.context)
            //pass our network request stored in variable jsonArray
            requestQueue.add(jsonArray)

    }

        //we start the thread containing the process
        backgroundThread.start()

    }

    //handles all the json data from the Json object request that we use to get all the data of our recipes
    @SuppressLint("SuspiciousIndentation")
    fun parseJson(jsonData:String):String {

            //new instance of json object containing our json data
            val jsonObject = JSONObject(jsonData)

            //we retrieve data from a json array of recipes called "hits"
            val arrayOfRecipes = jsonObject.getJSONArray("hits")

            //instantiating our variables to store the data
            var recipeName:String
            var recipeImage:String
            var recipeSource:String
            var recipeUrl:String
            var recipeNutrients: RecipeNutrients

            //Makes sure the query retrieves at least one row of data, otherwise the app notifies the user
            if (arrayOfRecipes.length() > 0){

                //clears the array so its empty to store the data retrieved with every request

                if (recipes.isNotEmpty()){
                    recipes.clear()
                }

                //looping the json array 20 times to get data for 20 recipes
                for (i in 0 until 20){

                    //instantiating a new recipe nutrients object
                    recipeNutrients = RecipeNutrients(0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0)

                    //instantiating a new recipe object
                    val recipe = Recipe(0, "", "", ""," ", arrayListOf(), arrayListOf(), arrayListOf(), arrayListOf(), recipeNutrients)


                    //getting the data and storing it in every property of recipe object

                    recipeName = arrayOfRecipes.getJSONObject(i).getJSONObject("recipe").getString("label")
                    recipe.name = recipeName

                    recipeImage = arrayOfRecipes.getJSONObject(i).getJSONObject("recipe").getString("image")
                    recipe.image = recipeImage


                    recipeSource = arrayOfRecipes.getJSONObject(i).getJSONObject("recipe").getString("source")
                    recipe.source = recipeSource


                    recipeUrl = arrayOfRecipes.getJSONObject(i).getJSONObject("recipe").getString("url")
                    recipe.url = recipeUrl



                    //gets the nutrients object from json array of recipes
                    val nutrientsObject = arrayOfRecipes.getJSONObject(i).getJSONObject("recipe").getJSONObject("totalNutrients")

                    //getting the each property for our nutrients object
                    recipe.nutrients.calories = nutrientsObject.getJSONObject("ENERC_KCAL").getDouble("quantity")
                    recipe.nutrients.fats = nutrientsObject.getJSONObject("FAT").getDouble("quantity")
                    recipe.nutrients.carbs = nutrientsObject.getJSONObject("CHOCDF").getDouble("quantity")
                    recipe.nutrients.fiber = nutrientsObject.getJSONObject("FIBTG").getDouble("quantity")
                    recipe.nutrients.sugar = nutrientsObject.getJSONObject("SUGAR").getDouble("quantity")
                    recipe.nutrients.protein = nutrientsObject.getJSONObject("PROCNT").getDouble("quantity")
                    recipe.nutrients.cholesterol = nutrientsObject.getJSONObject("CHOLE").getDouble("quantity")
                    recipe.nutrients.sodium = nutrientsObject.getJSONObject("NA").getDouble("quantity")



                    //we retrieve the data with the recipe ingredients from a json array
                    val recipeIngredientsArray= arrayOfRecipes.getJSONObject(i).getJSONObject("recipe").getJSONArray("ingredients")


                    //we loop for as many ingredients as there are inside the array for each recipe
                    for (z in 0 until recipeIngredientsArray.length()){

                        //we now instantiate a new recipe ingredient object and fill the properties with the data
                        var  recipeIngredient = RecipeIngredient("", "", "", 0.0, "", "")

                        val ingredientObject = recipeIngredientsArray.getJSONObject(z)

                        val ingredientNameItem = ingredientObject.getString("food")
                        val ingredientFoodId = ingredientObject.getString("foodId")
                        var ingredientMeasure = ingredientObject.getString("measure")
                        var ingredientFullLine = ingredientObject.getString("text")
                        var ingredientImage = ingredientObject.getString("image")
                        val ingredientQuantity = ingredientObject.getDouble("quantity")
                        recipeIngredient.foodId = ingredientFoodId
                        recipeIngredient.ingredientName = ingredientNameItem
                        recipeIngredient.measure = ingredientMeasure
                        recipeIngredient.quantity = ingredientQuantity
                        recipeIngredient.ingredientImage = ingredientImage
                        recipeIngredient.ingredientFullLine = ingredientFullLine


                        //we add the recipe ingredient object with all its properties to our recipe object
                        recipe.ingredients.add(recipeIngredient)

                    }

                    //all the data has been transfered now to our recipe object and we can add it to our recipes array

                    recipes.add(recipe)

                }

                //Building the recyclerView for the main activity

                //code adapted from Pushpender007 - Android recycler View (2021)


                //instantiates our recycler view form our xml file
                val recyclerView = findViewById<RecyclerView>(R.id.recipesRecyclerView)

                recyclerView.layoutManager = LinearLayoutManager(this)

                //instantiates a new Array list with our recycler model
                val data = ArrayList<ItemsViewModel>()

                //loops to add 20 recipes to our array list of recycler view items
                for (i in 0 until 20) {

                    data.add(ItemsViewModel( recipes[i].image, recipes[i].name, recipes[i].source, recipes[i].nutrients.calories))
                }

                // This will pass the ArrayList to our Adapter
                val adapter = CustomAdapter(data, this)

                // Setting the Adapter with the recyclerview
                recyclerView.adapter = adapter

                //notifies the adapter of data changes
                adapter.notifyDataSetChanged()

            }
            //lets the user know if the json data array was empty because there were no matches
            else
                Toast.makeText(this, "ups!, there are no recipes that match that criteria", Toast.LENGTH_SHORT ).show()

            //returns a string needed for our parseJson fuction
            return ""

    }

    //sets an on click listener for each item of the recycler view
    override fun onItemClick(position: Int) {

        //when item (recipe) is click, an intent will be thrown to start the recipe activity, passing all the needed data to it
        Intent(this, RecipeActivity::class.java).also {


                    it.putExtra("EXTRA_NAME", recipes[position].name)
                    it.putExtra("EXTRA_IMAGE", recipes[position].image)
                    it.putExtra("EXTRA_SOURCE", recipes[position].source)
                    it.putExtra("EXTRA_URL", recipes[position].url)
                    it.putExtra("EXTRA_INGREDIENTS", recipes[position].ingredients)
                    it.putExtra("EXTRA_NUTRIENTS", recipes[position].nutrients)
                    it.putExtra("EXTRA_RECIPE", recipes[position])

                    startActivity(it)

        }

    }


    //takes the input from the search bar and use it in our url for our API request
    private fun searchString():String{

        val searchInput:String = "q=" + binding.editTextRecipeSearch.text.toString()
        searchInput.lowercase()
        return searchInput

    }

    //takes the input from the diet type spinner and use it in our url for our API request
    private fun dietTypeFilter():String{

        var dietFilter = "&diet=$dietTypeFilter"

        if (dietTypeFilter == "diet type (none selected)"){dietFilter = ""}

        return dietFilter
    }

    //takes the input from the health label filter and use it in our url for our API request
    private fun healthLabelFilter():String{

        var healthLabel = "&health=$healthTypeFilter"

        if (healthTypeFilter== "health label (none selected)"){healthLabel = ""}

        return healthLabel
    }


    //sets the request for a random recipes, used when we press the random button on main screen
    //proccess similar to our previous request
    //run on a background thread as well
    private fun getRandomRecipe(view: View) {

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = ProgressBar.VISIBLE

        val backgroundThread = Thread{

            val jsonArray = JsonObjectRequest(Request.Method.GET, urlRandomRecipe, null, { response ->
                try {

                    parseJson(response.toString())
                    progressBar.visibility = ProgressBar.INVISIBLE

                } catch (e: JSONException){
                    e.printStackTrace()
                    Toast.makeText(view.context, "ups, that didn't go well...", Toast.LENGTH_SHORT).show()
                }

            }
            )
            { }

            val requestQueue: RequestQueue = Volley.newRequestQueue(view.context)
            requestQueue.add(jsonArray)

        }

        backgroundThread.start()

    }


    //sets the request when app is in fun mode, used when we press the random button on main screen
    //proccess similar to our previous request
    //run on a background thread as well
    private fun getRandomRecipeFunMode(view: View) {

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = ProgressBar.VISIBLE

        val backgroundThread = Thread{

            urlRandomRecipe = randomAlcoholRecipe()



            val jsonArray = JsonObjectRequest(Request.Method.GET, urlRandomRecipe, null, { response ->
                try {
                    parseJson(response.toString())
                    progressBar.visibility = ProgressBar.INVISIBLE

                } catch (e: JSONException){
                    e.printStackTrace()
                    Toast.makeText(view.context, "ups, that didn't go well...", Toast.LENGTH_SHORT).show()
                }

            }
            )
            { }

            val requestQueue: RequestQueue = Volley.newRequestQueue(view.context)
            requestQueue.add(jsonArray)
        }

        backgroundThread.start()

    }


    //this function will return an url to use in our fun mode request
    //url contains a random alcoholic ingredient from the 9 set each time is called
    private fun randomAlcoholRecipe():String{

        val randomNumber = (1..10).random()
        var alcoholicIngredient = ""


        when (randomNumber){

            1 -> alcoholicIngredient = "vodka"
            2 -> alcoholicIngredient = "gin"
            3 -> alcoholicIngredient = "whiskey"
            4 -> alcoholicIngredient = "brandy"
            5 -> alcoholicIngredient = "tequila"
            6 -> alcoholicIngredient = "beer"
            7 -> alcoholicIngredient = "wine"
            8 -> alcoholicIngredient = "cider"
            9 -> alcoholicIngredient = "mezcal"


        }

        //changes the url used in the fun mode request

        var alcoholURL = "https://api.edamam.com/api/recipes/v2?type=public&q=$alcoholicIngredient&app_id=254d3b24&app_key=22a50f4621cac2f07d1cdc979bbfd3f5&random=true&field=uri&field=label&field=image&field=source&field=url&field=dietLabels&field=healthLabels&field=ingredientLines&field=ingredients&field=totalNutrients&dishType=Drinks"

        //returns the url
        return alcoholURL


    }

    //instantiates the videoView and uses the MediaController class to play a video and create controllers for it
    //code adapted from Meghan Gill - VideoView Tutorial with Example in Android Studio (12/06/2021)
    private fun playFunVideo(){

        val mediaController = MediaController(this)

        val funModeVideo = findViewById<VideoView>(R.id.videoView)

        funModeVideo.setMediaController(mediaController)
        mediaController.setAnchorView(funModeVideo)
        val videoPath = "android.resource://" + packageName + "/" + R.raw.failsfinal
        val uri = Uri.parse(videoPath)
        funModeVideo.setVideoURI(uri)
        funModeVideo.start()

    }


}













































































































