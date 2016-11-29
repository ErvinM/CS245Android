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

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class PlayGame extends AppCompatActivity implements View.OnClickListener {

    /**
     * The current score for the game
     */
    private int score;
    /**
     * The current score for the game
     */
    private int score_flag = 0;

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
     * The user's initials
     */
    private String initials;

    /**
     * Name of the file to write high scores to
     */
    private String highScoreListName;

    private MainFragment music;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        Button retry = (Button) findViewById(R.id.try_again_button);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firstCard != null){
                    firstCard.flip();
                    firstCard = null;
                }
                if(secondCard != null){
                    secondCard.flip();
                    secondCard = null;
                }
            }
        });
        //Gets the number of cards from the previous activity
        numCards = Integer.parseInt(getIntent().getStringExtra("NUMBER_OF_CARDS"));
        highScoreListName =  Integer.toString(numCards) + "_high_score.txt";// high score file name
        cardWords = new String[numCards];
        //wordPositions = new int[numCards];
        gridLayout = (GridLayout)findViewById(R.id.game_grid_layout);
        if (savedInstanceState != null){
            oldGame(savedInstanceState);
        }
        else{
            newGame();
        }
       /* music.start(this);
        Button muteButton = (Button) findViewById(R.id.menu_music_switch);
        muteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                music.stop();
            }
        });*/
    }

    /**
     * Reinitializes values on rotation.
     * @param savedInstanceState
     */
    private void oldGame(Bundle savedInstanceState)
    {
        Card temp;
        score = savedInstanceState.getInt("score");
        score_flag = savedInstanceState.getInt("score_flag");

        firstCard = (Card)savedInstanceState.getSerializable("first");
        secondCard = (Card)savedInstanceState.getSerializable("second");

        numCardsMatched = savedInstanceState.getInt("matches");
        setGridLayout();

        for(int c = 0; c < cards.length; c++) {
            temp = (Card) savedInstanceState.getSerializable("c" + Integer.toString(c));
            cards[c].cardValue = temp.cardValue;
            if(temp.usedState()) {
                cards[c].reFlip();
                cards[c].setUsed();
            }
            else if(temp.flipState())
                cards[c].reFlip();
        }
        if(firstCard != null)
            firstCard = cards[firstCard.row * columns + firstCard.column];
        if(secondCard != null)
            secondCard = cards[secondCard.row * columns + secondCard.column];

        if(score_flag == 1)
        {
            //if you want to load initials initilas is stills saved
            //initials = savedInstanceState.getSerializable("initials");
            int s = isHighScore();
            if(s != -1){
                highScore(s);
            }
        }
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
        score_flag = 0;
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

    AlertDialog dialog;
    /**
     * Creates an alert dialog, asking the user for initials
     * @param index
     */
    private void highScore(final int index) {
        score_flag = 1;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.high_score_prompt, null);

        //Overwritten later, so it doesn't do anything
        builder.setView(view)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do nothing
                    }
        });

        // Creates an alert dialog using the builder
        dialog = builder.create();

        final EditText userInitials = (EditText)view.findViewById(R.id.high_score_initials);

        //Used to overwrite the click for the alert dialog
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {

                Button okay = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                okay.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        initials = userInitials.getText().toString();
                        if(initials.length() < 3){
                            //do nothing
                        }
                        else {
                            addHighScores(index);
                            dialog.dismiss();
                        }

                    }
                });
            }
        });
        dialog.show();
    }

    /**
     * Adds initials to the high score section
     */
    private void addHighScores(int index){
        String fileContents = fileToString(highScoreListName); //Gets the contents from the file
        String[] individualContent = fileContents.split("\n");

        if(index > -1 && index < 4){
            if(index + 1 < 3) {
                individualContent[index + 1] = individualContent[index];
            }
            individualContent[index] = (initials + " " + score);
        }
        try {
            FileOutputStream fos = openFileOutput(highScoreListName, Context.MODE_PRIVATE);

            for(int a = 0; a < 3; a++){
                System.out.println(individualContent[a]);
                fos.write((individualContent[a] + "\n").getBytes());
            }
            fos.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Checks if the current score is higher than any other score in the high score list
     * @return index where high score belongs
     */
    private int isHighScore(){
        String fileContents = fileToString(highScoreListName); //Gets the contents from the file
        String[] individualScores = fileContents.split("\n");//Splits each individual score

        for(int a = 0; a < 3; a++){
            String[] temp = individualScores[a].split(" ");//Splits individual score into two parts
            //If the score is greater than a score currently in the list
            if(score > Integer.parseInt(temp[1])){
                return a;
            }
        }
        return -1;
    }

    /**
     * Reads the contents of a file in memory, and puts it into a string
     * @return string version of file contents
     */
    private String fileToString(String fileName){
        int letter; //Individual character
        String tempString = ""; //Entire file in string form

        try {
            FileInputStream reader = openFileInput(fileName);

            //Runs while there are characters in the file
            while((letter = reader.read()) != -1){
                tempString += Character.toString((char)letter);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tempString;
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
        //Checks to see if the game is over
        if(numCardsMatched >= numCards){
            int s = isHighScore();
            if(s != -1){
                highScore(s);
            }

        }

    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    @Override
    public void onPause(){

        dialog.dismiss();
        super.onPause();
    }

    /**
     * Saves date on destroy for rotation
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle  savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt("score", score);
        savedInstanceState.putInt("cardNum", numCards);

        savedInstanceState.putInt("matches", numCardsMatched);

        savedInstanceState.putInt("score_flag",score_flag);

        savedInstanceState.putSerializable("first", firstCard);
        savedInstanceState.putSerializable("second", secondCard);
        if(score_flag == 1)
            savedInstanceState.putSerializable("initials", initials);

        for(int c = 0; c < cards.length; c++)
            savedInstanceState.putSerializable("c"+Integer.toString(c), cards[c]);
    }
}
