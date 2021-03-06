/**************************************
 *   file:Card.java
 *   @author Ervin Maulas, Joel Woods, Jose Garcia, Alan Chen
 *   Class: CS 245 - Graphical User Interface
 *
 *   Assignment: Android Memory Game
 *   @version v1.0
 *   Date Last Modified: 26 November 2016
 *   Purpose: Specialized Version of the Button widget. Allows us to create a button to act like
 *   a card for the game
 *
 ***************************************/
package com.defaultusername.defaultusername;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatDrawableManager;
import android.widget.Button;
import android.widget.GridLayout;
import java.io.Serializable;

public class Card extends Button implements Serializable{

    /**
     * The row the card is in
     */
    protected int row;

    /**
     * The column the card is in
     */
    protected int column;

    /**
     * True if the card is showing the card value
     */
    private boolean isFlipped;

    /**
     * True if the card has already been matched with another
     */
    private boolean isUsed;

    /**
     * The string on the card
     */
    protected String cardValue;

    /**
     * Back image of the card
     */
    private Drawable cardBack;


    public Card(Context context, String cardValue, int r, int c) {
        super(context);

        row = r;
        column = c;
        isFlipped = false;
        isUsed = false;


        this.cardValue = cardValue;
        cardBack = AppCompatDrawableManager.get().getDrawable(context, R.drawable.card_back);

        setFace(cardBack);

        GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(row),
                GridLayout.spec(column));
        params.width = (int) getResources().getDisplayMetrics().density * 60;
        params.height = (int) getResources().getDisplayMetrics().density * 93;
        setLayoutParams(params);
    }

    /**
     * Returns a boolean stating whether or not the card is still in play
     *
     * @return isUsed
     */
    public boolean usedState() {
        return isUsed;
    }

    public boolean flipState() {
        return isFlipped;
    }

    /**
     * Sets the Card as used, so it won't be able to be chosen again
     */
    public void setUsed() {
        isUsed = true;
    }

    /**
     * Returns the word on the front of the card
     *
     * @return word on front of the card
     */
    public String getValue() {
        return cardValue;
    }

    /**
     * Called whenever the Card widget is pressed
     */
    public void flip() {
        if (isUsed)
            return;

        else if (isFlipped) {
            setText("");
            isFlipped = false;
            setFace(cardBack);
        } else {
            setFace(cardValue);
            isFlipped = true;
        }
    }

    /**
     * Called when reloading card due to rotation
     */
    public void reFlip() {
        if (isUsed)
            return;
        setFace(cardValue);
        isFlipped = true;
    }

    /**
     * Overloaded method which sets the card face to the back image
     *
     * @param face picture assigned to the card
     */
    private void setFace(Drawable face) {
        //If the API level is below 16, it uses the depreciated setBackgroundDrawable
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            //noinspection deprecation
            setBackgroundDrawable(face);
        } else {
            setBackground(face);
        }
    }

    /**
     * Overloaded method which sets the card face to the card value
     *
     * @param face the value of the card
     */
    private void setFace(String face) {
        setBackgroundResource(0);
        setText(face);
    }


}


