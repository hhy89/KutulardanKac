package com.hhy.game.dodgebox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    // imagebuttons & lists
    private ArrayList<ImageButton> backgrounds = new ArrayList<>();
    private ArrayList<ImageButton> characters = new ArrayList<>();
    ImageButton background1, background2, background3, background4;
    ImageButton character1, character2;
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // for fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_settings);

        // imagebuttons
        background1 = findViewById(R.id.background1);
        background1.setOnClickListener(this);
        background2 = findViewById(R.id.background2);
        background2.setOnClickListener(this);
        background3 = findViewById(R.id.background3);
        background3.setOnClickListener(this);
        background4 = findViewById(R.id.background4);
        background4.setOnClickListener(this);
        character1 = findViewById(R.id.character1);
        character1.setOnClickListener(this);
        character2 = findViewById(R.id.character2);
        character2.setOnClickListener(this);
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(this);

        // image buttons lists
        backgrounds.add(background1);
        backgrounds.add(background2);
        backgrounds.add(background3);
        backgrounds.add(background4);
        characters.add(character1);
        characters.add(character2);

        // load for old chosens
        borderBackground(loadBackground());
        borderCharacter(loadCharacter());

        // for keeping screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.background1:
                borderBackground(0);
                saveBackground(0);
                break;
            case R.id.background2:
                borderBackground(1);
                saveBackground(1);
                break;
            case R.id.background3:
                borderBackground(2);
                saveBackground(2);
                break;
            case R.id.background4:
                borderBackground(3);
                saveBackground(3);
                break;
            case R.id.character1:
                borderCharacter(0);
                saveCharacter(0);
                break;
            case R.id.character2:
                borderCharacter(1);
                saveCharacter(1);
                break;

            // backbutton
            case R.id.backButton:
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    // chosen background
    private void borderBackground(int id) {
        for (ImageButton background : backgrounds) {
            background.setBackgroundResource(0);
        }
        backgrounds.get(id).setBackgroundResource(R.drawable.border);
    }

    // chosen character
    private void borderCharacter(int id) {
        for (ImageButton character : characters) {
            character.setBackgroundResource(0);
        }
        characters.get(id).setBackgroundResource(R.drawable.border);
    }

    // save background
    private void saveBackground(int id) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("dodgebox", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("background", id);
        editor.apply();
    }

    // load last chosen background
    private int loadBackground() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("dodgebox", MODE_PRIVATE);
        return sharedPreferences.getInt("background", 0);
    }

    // save character
    private void saveCharacter(int id) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("dodgebox", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("character", id);
        editor.apply();
    }

    // load last chosen character
    private int loadCharacter() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("dodgebox", MODE_PRIVATE);
        return sharedPreferences.getInt("character", 0);
    }

    @Override
    // on back pressed
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(intent);
    }
}
