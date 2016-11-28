/**************************************
 *   file:PlayGame.java
 *   @author Ervin Maulas, Joel Woods, Jose Garcia, Alan Chen
 *   Class: CS 245 - Graphical User Interface
 *
 *   Assignment: Android Memory Game
 *   @version v1.0
 *   Date Last Modified: 26 November 2016
 *   Purpose: Activity where the main game is played.
 *
 ***************************************/
package com.defaultusername.defaultusername;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Random;

public class PlayGame extends AppCompatActivity implements View.OnClickListener {

    /**
     * The current score for the game
     */
    private int score;

    /**
     * The number of cards for this instance of the game
     */
    private int numCards;

    /**
     * Number of cards that have been matched
     */
    private int numCardsMatched;

    /**
     * The card objects needed for the game
     */
    private Card[] cards;

    /**
     * Array storing the possible words that can be given to each card
     */
    private String[] cardWords;

    /**
     * GridLayout for the cards
     */
    private GridLayout gridLayout;

    /**
     * Number of rows in the grid
     */
    private int rows;

    /**
     * Number of columns in the grid
     */
    private int columns;

    /**
     * The first card selected by the user
     */
    private Card firstCard;

    /**
     * The second card selected by the user
     */
    private Card secondCard;

    /**
     *  This will store the initials of top 3 highscore from the highscore file.
     */
    private String[] highScoreInitials;

    /**
     * This will store the score of the top 3 highscore from the highscore file
     */
    private int[] highScoreScore;

    /**
     * Stores the name of the file storing the high scores
     */
    private String highScoreListName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        Button retry = (Button) findViewById(R.id.try_again_button);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                highScore();
//                if(firstCard != null){
//                    firstCard.flip();
//                    firstCard = null;
//                }
//                if(secondCard != null){
//                    secondCard.flip();
//                    secondCard = null;
//
//                }
            }
        });
        //Gets the number of cards from the previous activity
        numCards = Integer.parseInt(getIntent().getStringExtra("NUMBER_OF_CARDS"));
        cardWords = new String[numCards]; //Allocates memory for the card words
        gridLayout = (GridLayout)findViewById(R.id.game_grid_layout);

        newGame();
    }

    /**
     * Populates word array with card values and randomizes the order
     */
    private void populateCardWords(){
        Random random = new Random(System.currentTimeMillis());
        try {
            InputStreamReader reader = new InputStreamReader(getAssets().open("cardValues.txt"));
            BufferedReader br = new BufferedReader(reader);

            for (int i = 0; i < numCards; i++){
                cardWords[i++] = br.readLine(); //Adds word to array and increases index
                cardWords[i] = cardWords[i - 1]; //Adds same word to next index
            }
            br.close();
            reader.close();
        }
        catch (Exception e){
            System.err.println("There was an error");
        }
        //Mixes up the array of card words
        for (int i = cardWords.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            String temp = cardWords[index];
            cardWords[index] = cardWords[i];
            cardWords[i] = temp;
        }
    }
    /**
     * Sets the grid layout for the game activity
     */
    private void setGridLayout(){
        //Gets the current screen orientation
        int orientation = getResources().getConfiguration().orientation;

        //Sets the rows and columns for different configurations in landscape mode
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            if (numCards <= 12) {
                rows = 2;
                columns = numCards / 2;
            }
            else if (numCards % 4 == 0) {
                columns = 7;
                rows = 3;
            }
            // For 14, and 18 cards
            else {
                columns = 6;
                rows = 3;
            }

        }
        //Sets the rows and columns for different configurations in portrait mode
        else {
            //Makes even number of rows and columns for any card count that is 10 or lower
            if (numCards <= 10) {
                rows = 2;
                columns = numCards / 2;
            }
            //Any number > 10 and divisible by 4
            else if (numCards % 4 == 0) {
                columns = 4;
                rows = numCards / 4;
            }
            // For 14 and 18 cards
            else {
                columns = 4;
                rows = (numCards / 4) + 1;
            }
        }
        gridLayout.setColumnCount(columns);
        gridLayout.setRowCount(rows);
        gridLayout.removeAllViews();
        cards = new Card[numCards];

        for(int r = 0; r < rows; r++){
            for(int c = 0; c < columns; c++){
                Card tempCard; //Stores the address of a card object temporarily
                int cardIndex = (r * columns) + c;//More legible index for card array

                //Checks for special cases for 14 and 18 cards
                if((numCards == 14 || numCards == 16 || numCards == 20) &&
                        (r == rows - 1 && c > 1) &&
                        orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    break; // breaks out of the loop
                }

                //Checks for special cases for 14 and 18 cards
                if((numCards == 14 || numCards == 18) &&
                        (r == rows - 1 && c > 1) &&
                        orientation == Configuration.ORIENTATION_PORTRAIT){
                    break; // breaks out of the loop
                }
                tempCard = new Card(this, cardWords[cardIndex], r, c);
                tempCard.setOnClickListener(this);
                cards[cardIndex] = tempCard;
                gridLayout.addView(tempCard);
            }
        }
    }
    /**
     * Starts a new game for this activity
     */
    private void newGame(){
        score = 0;
        numCardsMatched = 0;
        firstCard = secondCard = null;

        populateCardWords();
        setGridLayout();
    }
    /**
     * Instantly flips all the cards on the board, ending the game
     */
    private void endGame(){
        //Returns from method when all the cards are already flipped
        if(numCardsMatched >= numCards) {
           //check for high_score_prompt
            highScore();
            //else
            return;
        }
        //Flips card if the user has two wrong choices when they end the game
        if(firstCard != null) {
            firstCard.flip();
            firstCard = null;
        }
        //Flips cards if the user has two wrong choices when they end the game
        if(secondCard != null) {
            secondCard.flip();
            secondCard = null;
        }
        //Flips all the cards on the board
        for(int a = 0; a < numCards;a++){
            cards[a].flip();
            cards[a].setUsed();
            numCardsMatched++;
        }
    }
    private void highScore()
    {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.high_score_prompt);
        dialog.show();
    }

    /**
     * Check if the current score is a high score comparing against the highscore file.
     */
    private boolean isHighScore(int curScore)
    {
        try {
            highScoreListName = Integer.toString(numCards) + "_high_score.txt";// high score file name
            String line;
            int highScoreRecord = 3; // 3 records in each highscore file
            int record = 0;
            InputStreamReader reader = new InputStreamReader(getAssets().open(highScoreListName));
            BufferedReader br = new BufferedReader(reader);
            while ((line = br.readLine()) != null) {
                String[] vals = line.trim().split("\\s+");

                if (highScoreInitials == null) {
                    highScoreInitials = new String[highScoreRecord];
                }
                if (highScoreScore == null) {
                    highScoreScore = new int[highScoreRecord];
                }

                highScoreInitials[record] = vals[0];
                highScoreScore[record] = Integer.parseInt(vals[1]);

                record++;
            }
            br.close();
            reader.close();
            for (int scores : highScoreScore) {
                if (scores > curScore) {
                    return true;
                }
            }
        } catch ( Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     *  This method will take in the current highscore and inserted into the correct spot in the highscore file
     */
    public void writeHighScore(String curHighInitial, int curHighScore)
    {
        int position = -1;
        int tempScore = -1, tempScore2 = -1;
        String tempInitial2 = "", tempInitial = "";
        for (int i = 0; i < highScoreScore.length; i++) {
            if (curHighScore > highScoreScore[i]) {
                position = i;
                break;
            }
    }
        try {

            PrintWriter pw = new PrintWriter(highScoreListName);
            for (int a = 0; a < highScoreScore.length; a++) {
                if ( a == position ) {
                    tempScore = highScoreScore[a];
                    tempInitial = highScoreInitials[a];
                    highScoreScore[a] = curHighScore;
                    highScoreInitials[a] = curHighInitial;

                }
                else if ( tempScore != -1 && tempInitial != "") {
                    tempScore2 = tempScore;
                    tempInitial2 = tempInitial;
                    tempScore = highScoreScore[a];
                    tempInitial = highScoreInitials[a];
                    highScoreScore[a] = tempScore2;
                    highScoreInitials[a] = tempInitial2;
                }
            }
            for(int b = 0; b < highScoreScore.length; b++) {
                pw.println(highScoreInitials + " " + highScoreScore);
            }
            pw.close();
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a option menu for the user to select options from
     * @param menu menu object
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
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
        switch (item.getItemId()) {
            case R.id.menu_new_game:
                newGame();
                return true;

            case R.id.menu_end_game:
                endGame();
                return true;

            case R.id.menu_music_switch:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * OnClick method for the Card buttons
     * @param view object
     */
    @Override
    public void onClick(View view) {
        Card currentCard = (Card) view;

        //Does nothing if the card has already been matched with a partner
        if(currentCard.usedState()){
            return;
        }

        //Sets attributes if the card selected is the first card
        if(firstCard == null){
            firstCard = currentCard;
            firstCard.flip();
        }
        //Sets attributes if the card selected is the second card
        else if(secondCard == null && currentCard != firstCard){
            secondCard = currentCard;
            secondCard.flip();

            //Checks if the first and second card have the same value
            if(firstCard.getValue().equals(secondCard.getValue())){
                firstCard.setUsed();
                firstCard = null;
                secondCard.setUsed();
                secondCard = null;

                score += 2;
                numCardsMatched += 2;
                System.out.println(score);
            }
            else{
                if(score > 0)
                    score--;
                System.out.println(score);

            }
        }
        if(numCardsMatched >= numCards){
            System.out.println("Game Over");
        }

    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}
