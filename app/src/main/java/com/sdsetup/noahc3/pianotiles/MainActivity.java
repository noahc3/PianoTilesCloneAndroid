package com.sdsetup.noahc3.pianotiles;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {

    Random rng = new Random(); //init a random object


    int score = 0;
    boolean gameDone = false; //used to prevent the player from continuing to press tiles after they have failed.
    ImageButton[][] tiles = new ImageButton[4][4]; //2d array of tile buttons
    Boolean[][] tileTypes = new Boolean[4][4]; //2d array of booleans to determine whether a tile is black or white (correct or incorrect)
    MediaPlayer[] players = new MediaPlayer[4]; //media players to play different piano notes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //populate the tiles array with the tile buttons
        tiles[0][0] = findViewById(R.id.tile0);
        tiles[0][1] = findViewById(R.id.tile1);
        tiles[0][2] = findViewById(R.id.tile2);
        tiles[0][3] = findViewById(R.id.tile3);
        tiles[1][0] = findViewById(R.id.tile4);
        tiles[1][1] = findViewById(R.id.tile5);
        tiles[1][2] = findViewById(R.id.tile6);
        tiles[1][3] = findViewById(R.id.tile7);
        tiles[2][0] = findViewById(R.id.tile8);
        tiles[2][1] = findViewById(R.id.tile9);
        tiles[2][2] = findViewById(R.id.tile10);
        tiles[2][3] = findViewById(R.id.tile11);
        tiles[3][0] = findViewById(R.id.tile12);
        tiles[3][1] = findViewById(R.id.tile13);
        tiles[3][2] = findViewById(R.id.tile14);
        tiles[3][3] = findViewById(R.id.tile15);

        //create the mediaplayers. they are reusable.
        players[0] = MediaPlayer.create(this, R.raw.b4);
        players[1] = MediaPlayer.create(this, R.raw.a4);
        players[2] = MediaPlayer.create(this, R.raw.g4);
        players[3] = MediaPlayer.create(this, R.raw.c4);

        //for every tile EXCEPT the bottom row, set their tile type to false and the color to white
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 4; x++) {
                tileTypes[y][x] = false;
                tiles[y][x].setImageResource(R.drawable.whitesquare);
            }

            //pick a random tile in the row to set to black
            int x = rng.nextInt(4);
            tileTypes[y][x] = true;
            tiles[y][x].setImageResource(R.drawable.blacksquare);
        }
    }

    //play a piano note based on the tile column
    private void PlaySound(int x) {
        players[x].seekTo(750); //this 750ms delay is to negate the delay in the audio file that i didnt feel like trimming out
        players[x].start();
    }

    //run when a tile in the second-from-bottom row is pressed
    public void tile_Click(View v) {
        if (!gameDone) { //dont run if the game has already finished

            //figure out which tile was pressed based on the id
            int x = 0;
            switch(v.getId()) {
                case R.id.tile8:
                    x = 0;
                    break;
                case R.id.tile9:
                    x = 1;
                    break;
                case R.id.tile10:
                    x = 2;
                    break;
                case R.id.tile11:
                    x = 3;
                    break;
                default:
                    x = 0;
                    break;
            }

            if (!tileTypes[2][x]) { //if the tile pressed was NOT the correct tile
                gameDone = true; //end the game
                tiles[2][x].setImageResource(R.drawable.redsquare); //set the tile to red

                //play every note as a "piano smashing" effect
                for(MediaPlayer player : players) {
                    player.seekTo(750);
                    player.start();
                }

                //after 800ms, run the game done activity and end this activity. pass the score to the game done activity.
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        this.cancel(); //stops the timer from repeating
                        startActivity(new Intent(MainActivity.this, GameDoneActivity.class).putExtra("SCORE", score));
                        finish();
                    }
                }, 800);
            } else { //if the tile pressed was the correct tile
                PlaySound(x); //play the sound based on the column
                score++; //increment score
                ((TextView)findViewById(R.id.txtScore)).setText("" + score); //update score text

                //to shift each row down:
                //for every tile EXCEPT the top row:
                for(int y = 3; y > 0; y--) {
                    for (x = 0; x < 4; x++) {
                        if (tileTypes[y-1][x]) { //if the tile in the row above at the same x is black:
                            tileTypes[y][x] = true; //set the value accordingly
                            if (y != 3) {  //IF the current row is NOT the bottom row
                                tiles[y][x].setImageResource(R.drawable.blacksquare); //set the tile to black
                            } else { //if the current row IS the bottom row
                                tiles[y][x].setImageResource(R.drawable.greysquare); //set the tile to gray to indicate it has been pressed already.
                            }

                        } else { //if the tile in the row above at the same x is white
                            tileTypes[y][x] = false; //set the value accordingly
                            tiles[y][x].setImageResource(R.drawable.whitesquare); //set the tile to white
                        }
                    }
                }

                //for every tile in the top row
                for (x = 0; x < 4; x++) {
                    tiles[0][x].setImageResource(R.drawable.whitesquare); //set the tile to white
                    tileTypes[0][x] = false; //disable correct tiles
                }

                //pick a random tile in the top row to set to black
                int rand = rng.nextInt(4);
                tiles[0][rand].setImageResource(R.drawable.blacksquare);
                tileTypes[0][rand] = true;
            }
        }

    }


}
