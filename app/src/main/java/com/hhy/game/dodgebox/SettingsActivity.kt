package com.hhy.game.dodgebox

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton

class SettingsActivity : Activity(), View.OnClickListener {
    // imagebuttons & lists
    private val backgrounds = ArrayList<ImageButton>()
    private val characters = ArrayList<ImageButton>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // for fullscreen
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_settings)

        // imagebuttons
        findViewById<ImageButton>(R.id.background1).also {
            it.setOnClickListener(this)
            backgrounds.add(it)
        }
        findViewById<ImageButton>(R.id.background2).also {
            it.setOnClickListener(this)
            backgrounds.add(it)
        }
        findViewById<ImageButton>(R.id.background3).also {
            it.setOnClickListener(this)
            backgrounds.add(it)
        }
        findViewById<ImageButton>(R.id.background4).also {
            it.setOnClickListener(this)
            backgrounds.add(it)
        }
        findViewById<ImageButton>(R.id.character1).also {
            it.setOnClickListener(this)
            characters.add(it)
        }
        findViewById<ImageButton>(R.id.character2).also {
            it.setOnClickListener(this)
            characters.add(it)
        }
        findViewById<ImageButton>(R.id.backButton).also {
            it.setOnClickListener(this)
        }

        // load for old chosens
        borderBackground(loadBackground())
        borderCharacter(loadCharacter())

        // for keeping screen on
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.background1 -> saveBackground(0)
            R.id.background2 -> saveBackground(1)
            R.id.background3 -> saveBackground(2)
            R.id.background4 -> saveBackground(3)
            R.id.character1 -> saveCharacter(0)
            R.id.character2 -> saveCharacter(1)
            R.id.backButton -> {
                val intent = Intent(this, MenuActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            else -> {
            }
        }
    }

    // chosen background
    private fun borderBackground(id: Int) {
        for (background in backgrounds) {
            background.setBackgroundResource(0)
        }
        backgrounds[id].setBackgroundResource(R.drawable.border)
    }

    // chosen character
    private fun borderCharacter(id: Int) {
        for (character in characters) {
            character.setBackgroundResource(0)
        }
        characters[id].setBackgroundResource(R.drawable.border)
    }

    // save background
    private fun saveBackground(id: Int) {
        borderBackground(id)
        val sharedPreferences = getSharedPreferences("dodgebox", Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt("background", id).apply()
    }

    // load last chosen background
    private fun loadBackground(): Int {
        val sharedPreferences = getSharedPreferences("dodgebox", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("background", 0)
    }

    // save character
    private fun saveCharacter(id: Int) {
        borderCharacter(id)
        val sharedPreferences = getSharedPreferences("dodgebox", Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt("character", id).apply()
    }

    // load last chosen character
    private fun loadCharacter(): Int {
        val sharedPreferences = getSharedPreferences("dodgebox", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("character", 0)
    }

    // on back pressed
    override fun onBackPressed() {
        val intent = Intent(this, MenuActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}