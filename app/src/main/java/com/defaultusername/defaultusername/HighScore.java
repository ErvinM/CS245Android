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
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

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

    private void displayHighScore(){
        try {
            InputStreamReader reader = new InputStreamReader(getAssets().open(highScoreListName));
            BufferedReader br = new BufferedReader(reader);
            for(int i = 0; i < 3; i++){
                highScoreList[i] = br.readLine();
            }
            br.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String tempString = ""; //Used to store top 3 high scores in string format
        for(String score : highScoreList){
            tempString += (score + "\n");
        }
        scores.setText(tempString);


    }


}
