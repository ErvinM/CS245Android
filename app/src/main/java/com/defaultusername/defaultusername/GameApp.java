/**************************************
 *   file:GameApp.java
 *   @author Ervin Maulas, Joel Woods, Jose Garcia, Alan Chen
 *   Class: CS 245 - Graphical User Interface
 *
 *   Assignment: Android Memory Game
 *   @version v1.0
 *   Date Last Modified: 26 November 2016
 *   Purpose: Initial Screen. Lets the user choose how many cards they want in their memory game
 *
 ***************************************/

package com.defaultusername.defaultusername;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class GameApp extends AppCompatActivity{

    /**
     * Spinner used to display to user number of card choices
     */
    private Spinner gameSpinner;

    /**
     * Button used to confirm user choice
     */
    private Button startButton;

    /**
     * Button used to navigate to high score screen
     */
    private Button highScoreButton;

    /**
     * String storing which choice the user has picked
     */
    private String userChoice;

    /**
     * Adapter used for the spinnner
     */
    private ArrayAdapter<CharSequence> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //Calls the method from parent class
        setContentView(R.layout.initial_screen);

        gameSpinner = (Spinner)findViewById(R.id.spinner_num_cards);
        // Create an ArrayAdapter using Start_spinner_values
        adapter = ArrayAdapter.createFromResource(this,
                R.array.start_spinner_values, android.R.layout.simple_spinner_item);
        //Chooses the layout for the spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        gameSpinner.setAdapter(adapter);

        //Attaches button to corresponding button widget
        startButton = (Button)findViewById(R.id.start_button);
        //Specifies an event for the button
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Saves the current item in the spinner
                userChoice = gameSpinner.getSelectedItem().toString();
                Intent intent = new Intent(GameApp.this, PlayGame.class);
                intent.putExtra("NUMBER_OF_CARDS", userChoice); //allows us to pass number of cards
                startActivity(intent); //Moves to the game activity
            }
        });

        //Attaches high score button to corresponding button widget
        highScoreButton = (Button)findViewById(R.id.high_score_button);
        //Sets event for button
        highScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Saves the current item in the spinner
                userChoice = gameSpinner.getSelectedItem().toString();


            }
        });

    }

}
