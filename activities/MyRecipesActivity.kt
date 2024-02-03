package com.example.assignmenttest3.activities

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmenttest3.R
import com.example.assignmenttest3.adapters.MyRecipesRecyclerViewAdapter
import com.example.assignmenttest3.databinding.ActivityMyRecipesBinding
import com.example.assignmenttest3.models.MyRecipesViewModel
import com.example.assignmenttest3.objects.DataBaseHelper
import com.example.assignmenttest3.objects.Recipe


//This activity displays the list with the recipes saved on the local db
class MyRecipesActivity : AppCompatActivity(), MyRecipesRecyclerViewAdapter.OnItemClickListener, MyRecipesRecyclerViewAdapter.OnDeleteClickListener {


    private lateinit var binding: ActivityMyRecipesBinding
    private lateinit var myRecipesList:ArrayList<Recipe>
    private lateinit var databaseHelper: DataBaseHelper
    private lateinit var adapter:MyRecipesRecyclerViewAdapter
    private lateinit var recyclerview:RecyclerView
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var mediaPlayer: MediaPlayer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyRecipesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //we will use the database helper to handle the data from our local db

        databaseHelper = DataBaseHelper(this)

        //we use the method getAllRecipes from our database helper and place the recipes inside an arrayList
        myRecipesList = databaseHelper.allRecipes as ArrayList<Recipe>


        createDrawerMenu()
        //creates or updates our recycler view for this screen
        createOrUpdateAdapter()
        createFeedbackButton()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.my_recipes_menu, menu)

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)){
            return true
        }

        when(item.itemId){
            R.id.clearList ->{

                databaseHelper.deleteRecipeTable()
                myRecipesList.clear()
                createOrUpdateAdapter()
            }

        }

        return super.onOptionsItemSelected(item)
    }



    //set on click listener for each activity, when clicking on each recipe the webViewActivity is launched with the website for the recipe
    //using the recipe url
    override fun onItemClick(position: Int) {

        Intent(this, WebViewActivity::class.java).also {

            it.putExtra("EXTRA_URL", myRecipesList[position].url )

            startActivity(it)

        }

    }

    //when we call this function we remove an individual recipe from the db
    //function used on the trash can icon on each recipe card on recycler adapter
    override fun onDeleteClick(position: Int) {

        databaseHelper.deleteOne(myRecipesList[position])

        myRecipesList.removeAt(position)

        createOrUpdateAdapter()

    }

    //when we call this function we can share the url for given recipe
    //code adapted from official Android documentation
    //https://developer.android.com/training/sharing/send
    override fun onShareClick(position: Int) {

        val sendIntent: Intent = Intent().apply {

            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_SUBJECT, "Check out this cool recipe!")
            putExtra(Intent.EXTRA_TEXT, myRecipesList[position].url)
            type = "text/plain"

        }

        val shareIntent = Intent.createChooser(sendIntent, "Recipe share")
        startActivity(shareIntent)


    }

    private fun createOrUpdateAdapter() {

        //recycler view is populated on a similar way as we have seen on the rest

        recyclerview = findViewById(R.id.myRecipesRecyclerView)

        recyclerview.layoutManager = LinearLayoutManager(this)

        val data = ArrayList<MyRecipesViewModel>()

        for (i in 0 until myRecipesList.size) {
            data.add(
                MyRecipesViewModel(
                    myRecipesList[i].image,
                    myRecipesList[i].name,
                    myRecipesList[i].source,
                    R.drawable.ic_delete,
                    R.drawable.ic_share_mr
                )
            )
        }

        adapter = MyRecipesRecyclerViewAdapter(data, this, this)

        recyclerview.adapter = adapter
    }



    //drawer menu created as previously
    private fun createDrawerMenu(){

        val drawerLayout: DrawerLayout = binding.drawerLayout

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navView.setNavigationItemSelectedListener { it ->

            when(it.itemId){

                R.id.myHome -> Intent(this, MainActivity::class.java).also {
                    startActivity(it)
                }
                R.id.myRecipes -> {drawerLayout.closeDrawers()}

                R.id.preferences -> Intent(this, SettingsActivity::class.java).also {
                    startActivity(it) }

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


}