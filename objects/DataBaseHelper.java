package com.example.assignmenttest3.objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//creates a DataBaseHelper class that extends SQLiteOpenHelper, this will act as an link between our database and our activities
public class DataBaseHelper extends SQLiteOpenHelper {

    //code adapted from FreeCodeCamp.org, SQLite Database for Android - Full Course (2021)



    //creating an string to identify elements on our database more quickly
    public static final String RECIPE_TABLE = "RECIPE_TABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_RECIPE_NAME = "RECIPE_NAME";
    public static final String COLUMN_RECIPE_SOURCE = "RECIPE_SOURCE";
    public static final String COLUMN_RECIPE_URL = "RECIPE_URL";
    public static final String COLUMN_RECIPE_IMAGE = "RECIPE_IMAGE";


    public DataBaseHelper(@Nullable Context context) {
        super(context, "recipes.db", null, 1);
    }


    //we create the table each time the onCreate method is called
    @Override
    public void onCreate(SQLiteDatabase db) {

        //we can create sql statements and pass them as queries for our db using DataBaseHelper class

        String createTableStatement = "CREATE TABLE " + RECIPE_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_RECIPE_NAME + " TEXT, "
                + COLUMN_RECIPE_SOURCE + " TEXT, "
                + COLUMN_RECIPE_URL+ " TEXT, "
                + COLUMN_RECIPE_IMAGE + " TEXT)";
        db.execSQL(createTableStatement);

    }


    //method used for applying changes on the db globally, not needed in this app
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    //function that will populate a row with a recipe in our db table. Note that we have to pass it a recipe object
    public boolean addOne(Recipe recipe){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_RECIPE_NAME, recipe.getName());
        cv.put(COLUMN_RECIPE_SOURCE, recipe.getSource());
        cv.put(COLUMN_RECIPE_URL, recipe.getUrl());
        cv.put(COLUMN_RECIPE_IMAGE, recipe.getImage());

       long insert = db.insert(RECIPE_TABLE, null , cv);

        if (insert == -1){
            return false;
        }else {
            return true;
        }
    }

    //this function will delete the corresponding row for the recipe object that we pass it in
    public boolean deleteOne(Recipe recipe){

        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + RECIPE_TABLE + " WHERE " + COLUMN_ID + " = " + recipe.getId();

        Cursor cursor=  db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){

            return true;
        }else {return false;}

    }


    //this function will get all the recipes from the database and store them on a List of recipes
    public List<Recipe> getAllRecipes(){

        List<Recipe> favoriteRecipesList = new ArrayList<>();

        String queryString = "SELECT * FROM " + RECIPE_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
//            loop through the cursor (result set) and create new recipe objects
            do {

                int recipeID = cursor.getInt(0);
                String recipeName = cursor.getString(1);
                String recipeSource = cursor.getString(2);
                String recipeURL = cursor.getString(3);
                String recipeImage = cursor.getString(4);

                Recipe newFavoriteRecipe = new Recipe(recipeID, recipeName, recipeSource, recipeURL, recipeImage);
                favoriteRecipesList.add(newFavoriteRecipe);

            }while  (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return favoriteRecipesList;

    }


    //this function will delete the recipes table, clearing our list of recipes
    public void deleteRecipeTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + RECIPE_TABLE);
        onCreate(db);
        db.close();
    }

    //this function will return get the number of recipes inside the db
    public int getRecipeCount() {
        String countQuery = "SELECT * FROM " + RECIPE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    //passing a recipe name inside this function we can check if the passed recipe is already in the db
    public Boolean checkForName(String recipe) {
        String checkQuery = "SELECT * FROM " + RECIPE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(checkQuery, null);

        if (cursor.moveToFirst()){
            do {
                String recipeName = cursor.getString(1);
                if (Objects.equals(recipeName, recipe)){
                    return true;
                }
            } while (cursor.moveToNext());
        }

        // If we get here, we didn't find a matching recipe name in the cursor
        return false;

    }
}







