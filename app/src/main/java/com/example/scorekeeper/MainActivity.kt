package com.example.scorekeeper

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.scorekeeper.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)


        val badminton = findViewById<Button>(R.id.game_badminton)
        val cricket = findViewById<Button>(R.id.game_cricket)
        val football = findViewById<Button>(R.id.game_football)

        viewBinding.gameBadminton.setOnClickListener {
            callActualScoring(GAMESCORES.BADMINTON)
        }

        viewBinding.gameCricket.setOnClickListener{
            callActualScoring(GAMESCORES.CRICKET)
        }

        viewBinding.gameFootball.setOnClickListener{
            callActualScoring(GAMESCORES.FOOTBALL)
        }
    }

    private fun callActualScoring(game: GAMESCORES) {
        val intent = Intent(this, ActualScoringActivity::class.java).also {
            it.putExtra("GAME_NAME", game.name)
            startActivity(it)
        }

    }
}
