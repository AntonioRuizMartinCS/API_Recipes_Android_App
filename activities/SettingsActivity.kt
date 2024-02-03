package com.example.assignmenttest3.activities

import android.annotation.SuppressLint
import android.app.UiModeManager.MODE_NIGHT_NO
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.drawerlayout.widget.DrawerLayout
import androidx.preference.PreferenceManager
import com.example.assignmenttest3.R
import com.example.assignmenttest3.databinding.ActivitySettingsBinding


//Settings Activity - shared preferences screen
class SettingsActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var toggle: ActionBarDrawerToggle
    lateinit var mediaPlayer: MediaPlayer



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        applyPreferences()
        createDrawerMenu()
        createFeedbackButton()
    }

    //code adapted from JavaCourseDrive, Android Tutorials by JavaCourseDrive - Preference Change Listener | Shared Preferences (2021)
    override fun onDestroy() {
        super.onDestroy()

        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.app_bar_menu, menu)

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }



    //creating drawer menu like previously
    //code adapted from Foxandroid, Navigation Drawer with Fragments using Kotlin || Android Studio Tutorial - 2021 (2022)
    private fun createDrawerMenu(){

        val drawerLayout: DrawerLayout = binding.drawerLayout

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navView.setNavigationItemSelectedListener {

            when(it.itemId){

                R.id.myHome -> {
                    Intent(this, MainActivity::class.java).also {
                        startActivity(it)
                    }
                }

                R.id.myRecipes -> {
                    Intent(this, MyRecipesActivity::class.java).also {
                        startActivity(it)
                    }
                }

                R.id.preferences -> { drawerLayout.closeDrawers()
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

    //in this function we will get the default preferences for dark mode and apply them
    @SuppressLint("WrongConstant")
    private fun applyPreferences(){

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        val activateDarkMode = prefs.getBoolean("activate_dark_mode", false)

        if (activateDarkMode){
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
        }

        prefs.registerOnSharedPreferenceChangeListener(this)


    }

    //this function will register a change in the preferences and apply them without having to wait for the activity go through the on create method
    @SuppressLint("WrongConstant")
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

        val activateDarkMode = sharedPreferences?.getBoolean("activate_dark_mode", false)

        if (activateDarkMode == true){
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
        }
    }

}