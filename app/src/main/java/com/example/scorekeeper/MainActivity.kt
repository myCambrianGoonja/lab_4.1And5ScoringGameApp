package com.example.scorekeeper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val badminton = findViewById<Button>(R.id.game_badminton)
        val cricket = findViewById<Button>(R.id.game_cricket)
        val football = findViewById<Button>(R.id.game_football)

        badminton.setOnClickListener {
            callActualScoring("BADMINTON")
        }

        cricket.setOnClickListener{
            callActualScoring("CRICKET")
        }

        football.setOnClickListener{
            callActualScoring("FOOTBALL")
        }
    }

    private fun callActualScoring(game: String) {
        val intent = Intent(this, ActualScoring::class.java).also {
            it.putExtra("GAME_NAME", game)
            startActivity(it)
        }

    }
}