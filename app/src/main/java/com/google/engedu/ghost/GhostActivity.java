package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class GhostActivity extends ActionBarActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    TextView statusText;
    Button restartButton,challengeButton;
    TextView text;
    String wordFragment;
    boolean isWord=false,isSubstr=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);

        statusText = (TextView)findViewById(R.id.status);
        text = (TextView) findViewById(R.id.ghostText);
        restartButton = (Button) findViewById(R.id.restart);
        challengeButton = (Button)findViewById(R.id.challenge);
        wordFragment = "";

        challengeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                challengeComputer();
            }
        });

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onRestart(v);
            }
        });


        onStart(null);

        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new FastDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public boolean checkWord(String word){
        /*For string length >=4, check if the string is a complete word*/
        if(dictionary.isWord(word)&&word.length()>=4){
            isWord=true;
            TextView label = (TextView) findViewById(R.id.gameStatus);
            label.setText("You lose");
            return true;
        }
        /*When the string is not a complete word or is less than 4 characters,
        check if it is a valid prefix or substring
         */
        else {
            if(dictionary.isSubstring(word)){
                isSubstr = true;
                return true;
            }
            isWord=false;
        }
        return isWord;
    }


    /*Get the next longest word possible from current prefix,
    loop till end of current wordfragment and add the next letter from longestword
     */
    public char addNextLetter(){
        String longerWord = dictionary.getAnyWordStartingWith(wordFragment);
        int i;
        for(i = 0 ; i<wordFragment.length();i++);
        char nextLetter =  longerWord.charAt(i);
        return nextLetter;
    }


    /*If current wordfragment is not a valid substring, user wins (currently not possible)
    If current wordfragment is a complete word, user wins.
    If current wordfragment is a valid prefix, inform the user
     */
    public void challengeComputer(){
        if(!dictionary.isSubstring(wordFragment)){
            statusText.setText("You win!");
        }
        else if(dictionary.isWord(wordFragment)){
            statusText.setText("Defeat accepted, you win");
        }
        else{
            statusText.setText("Word exists from this prefix");
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event){
        keyCode = event.getKeyCode();
        if(keyCode >=29 && keyCode <=54){
            char pressedKey = (char) event.getUnicodeChar();
            /* Add the pressed key to wordfragment, set the display, check for validity
             */
            wordFragment = wordFragment + Character.toString(pressedKey);
            text.setText(wordFragment);
            checkWord(wordFragment);
            userTurn = false;
            Log.d("going to","computer turn");
            if(dictionary.isWord(wordFragment) || !isSubstr){
                statusText.setText("You lose, Computer wins");
            }
            //If wordfragment is valid, call computerTurn
            else {
                computerTurn();
            }

        }
        else{
            return super.onKeyUp(keyCode,event);
        }
        return true;
    }
    private void computerTurn() {
        String longerWord = "";
        TextView label = (TextView) findViewById(R.id.gameStatus);

        /*
            Execute random generation of first letter, if computer plays first
         */
        if(wordFragment.equals("")){
            Random r = new Random();
            char c = (char)(r.nextInt(26) + 'a');
            wordFragment+= Character.toString(c);
            Log.d("Word fragment ", wordFragment);
            text.setText(wordFragment);
        }
        else {
                //Simulate delay
                try{
                    Thread.sleep(100);
                }
                catch (Exception e){}

                // Do computer turn stuff then make it the user's turn again

                if (wordFragment.length() >= 4 && !isSubstr) {
                    statusText.setText("Computer Victory!");
                }

                longerWord = dictionary.getAnyWordStartingWith(wordFragment);


            /*
            If the generated longer word is same as the wordfragment or is null, no longer word exists
             */
                if (longerWord == null || longerWord.equals(wordFragment)) {
                    if(longerWord == null){
                        statusText.setText("Word with prefix doesn't exist, you lose");
                    }
                    else if(longerWord.equals(wordFragment)){
                        statusText.setText("This is a word, you lose");
                    }
                }
                else {
                    char nextLetter = addNextLetter();
                    wordFragment += Character.toString(nextLetter);
                    text.setText(wordFragment);
                    //Un-comment the following comment to automatically accept computer defeat
                   /* if(dictionary.isWord(wordFragment)){
                        statusText.setText("You win, computer loses");
                    }*/
                }
                userTurn = true;
                label.setText(USER_TURN);
        }

    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onRestart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        statusText.setText("");
        wordFragment = "";
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }


    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        wordFragment = "";
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }
}
