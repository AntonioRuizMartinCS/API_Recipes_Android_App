package com.example.assignmenttest3.objects;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.assignmenttest3.R;
import com.example.assignmenttest3.activities.RecipeActivity;

import org.jetbrains.annotations.NotNull;

import kotlin.Unit;

//ExampleDialog class extends AppCompactDialogFragment that allow us to create a pop up dialog fragment
public class ExampleDialog extends AppCompatDialogFragment {

    private MediaPlayer mediaPlayer;


    //code adapted from Coding in Flow, Simple Dialog with 1 Button - Android Studio Tutorial (2018)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        //creates a new dialog builder with the information needed inside
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Information")
                .setMessage("To exceed the limit of 5 saved recipes you need to upgrade to premium...")
                .setPositiveButton("Upgrade", new DialogInterface.OnClickListener() {

                    //sets an on click listener on the alert dialog button
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.siuuuu); // Initialize the MediaPlayer with the sound file
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mediaPlayer.release(); // Release the MediaPlayer after the sound has finished playing
                            }
                        });
                        mediaPlayer.start(); // Play the sound



                    }
                });

                //creates the builder

               return builder.create();

    }

    //what happens if you click outside the dialog
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        // Your onDismiss code here
        Toast.makeText(getActivity(), "Only messing, the recipe was added to your recipes", Toast.LENGTH_SHORT).show();
    }

}
