/**************************************
 *   file:HighScore.java
 *   @author Ervin Maulas, Joel Woods, Jose Garcia, Alan Chen
 *   Class: CS 245 - Graphical User Interface
 *
 *   Assignment: Android Memory Game
 *   @version v1.0
 *   Date Last Modified: 26 November 2016
 *   Purpose: Creates a class used to display the high scores to the user
 *
 ***************************************/
package com.defaultusername.defaultusername;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.FileInputStream;

public class HighScore extends AppCompatActivity{
    /**
     * TextView displaying the high scores
     */
    private TextView scores;

    /**
     * TextView displaying how many cards in the game
     */
    private TextView numScore;

    /**
     * Number of cards in the game
     */
    private int numCards;

    /**
     * Stores the high score list content
     */
    private String[] highScoreList;

    /**
     * Stores the name of the file storing the high scores
     */
    private String highScoreListName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //Calls the method from parent class
        setContentView(R.layout.high_score_screen);

        numCards = Integer.parseInt(getIntent().getStringExtra("NUMBER_OF_CARDS"));
        highScoreListName = Integer.toString(numCards) + "_high_score.txt";// high score file name
        highScoreList = new String[3];//Will store the top 3 high scores

        numScore = (TextView) findViewById(R.id.high_score_number);
        numScore.setText(Integer.toString(numCards));
        scores = (TextView) findViewById(R.id.high_score_names);

        displayHighScore();
    }

    /**
     * Reads names from file, and displays them to the screen
     */
    private void displayHighScore(){
        int letter;
        String tempString = "";

        try {
            FileInputStream reader = openFileInput(highScoreListName);

            while((letter = reader.read()) != -1){
                tempString += Character.toString((char)letter);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        scores.setText(tempString);
    }
    /**
     * Creates a option menu for the user to select options from
     * @param menu menu object
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_initial, menu);
        return true;
    }

    /**
     * Calls a method depending on what menu option is chosen
     * @param item object
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if(item.getItemId() == R.id.menu_music_switch){

            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }
}
