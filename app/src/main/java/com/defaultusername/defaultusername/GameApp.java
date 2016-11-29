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

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

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

    static protected MediaPlayer mPlayer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //Calls the method from parent class
        setContentView(R.layout.initial_screen);
        
        //mPlayer = new MediaPlayer(getActivity); //is this correct placement?

        populateMemory();
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
                Intent intent = new Intent(GameApp.this, HighScore.class);
                intent.putExtra("NUMBER_OF_CARDS", userChoice); //allows us to pass number of cards
                startActivity(intent); //Moves to the high score activity
            }
        });
        mPlayer = MediaPlayer.create(this, R.raw.test);
        mPlayer.start();
    }

    /**
     * Takes the high score files in the assets folder and populates them into internal memory
     * if they do not already exist in there
     */
    private void populateMemory(){
        String fileName = "_high_score.txt";// high score file name
        String tempFileName;

        try {
            for (int a = 4; a <= 20; a += 2) {
                tempFileName = (a + fileName);

                if(!doesFileExist(tempFileName)) {
                    FileOutputStream fos = openFileOutput(tempFileName, Context.MODE_PRIVATE);
                    fos.write(getFileContents(tempFileName).getBytes());
                    System.out.println(tempFileName + " has been written");
                    fos.close();
                }
                else
                    System.out.println("File exists");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @param fileName
     * @return
     */
    private String getFileContents(String fileName){
        String fileContents = "";
        try {
            InputStreamReader reader = new InputStreamReader(getAssets().open(fileName));
            BufferedReader br = new BufferedReader(reader);

            for(int a = 0; a < 3; a++){
                fileContents += br.readLine() + "\n";
            }
            reader.close();
            br.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("ERROR reading scores from file");
        }
        return fileContents;
    }

    /**
     * Checks if the file already exists in internal memory
     * @param name
     * @return
     */
    private boolean doesFileExist(String name){
        File file = getBaseContext().getFileStreamPath(name);
        return file.exists();
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
            if(mPlayer.isPlaying())
                mPlayer.pause();
            else
                mPlayer.start();
        return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }

}
