package com.example.scorekeeper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.ConsoleMessage
import android.widget.Button
import android.widget.TextView
import androidx.core.util.rangeTo
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.w3c.dom.Text
import kotlin.math.log


private enum class GAMESCROES(val gameName:String, val scoreRange:IntRange){
    CRICKET("Cricket", 0..300),
    FOOTBALL("Football", 0..9),
    BADMINTON("Badminton", 0..7)
}

class ActualScoring : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actual_scoring)


        //Getting the name of the sports selected
        val sportsName = getSportsName()

        addingPointsForTeams(sportsName, "A")
        addingPointsForTeams(sportsName, "B")
    }

    private fun getSportsName():GAMESCROES {
        val importedValue = intent.getStringExtra("GAME_NAME").toString()
        val sportsName: GAMESCROES = GAMESCROES.values().find { it.name == importedValue }?: GAMESCROES.BADMINTON
        var textViewName = findViewById<TextView>(R.id.nameOfTheGame).apply {
            text = sportsName.name
        }
        return sportsName
    }


    private fun addingPointsForTeams(sportsName: GAMESCROES, teamName: String) {
        lateinit var btnTeamScore:FloatingActionButton
        lateinit var scoreBoard:TextView
        var whosWinning = findViewById<TextView>(R.id.whosWinning)
        when(teamName) {
            "A" -> {
                btnTeamScore  = findViewById<FloatingActionButton>(R.id.floatingButtonTeamA)
                scoreBoard = findViewById<TextView>(R.id.teamAScore)
            }
            "B" -> {
                btnTeamScore  = findViewById<FloatingActionButton>(R.id.floatingButtonTeamB)
                scoreBoard = findViewById<TextView>(R.id.teamBScore)
            }
        }

        var scoreValue = 0
        whosWinning.text = "Let the game begin!"
        btnTeamScore.setOnClickListener {
            scoreValue++
            if (!sportsName.scoreRange.contains(scoreValue)) {
                scoreValue = 0
            }

            when (sportsName) {
                GAMESCROES.BADMINTON -> {
                    when (scoreValue) {
                        0 -> whosWinning.text = "Love all Lets play!"
                        6 -> scoreBoard.text = "Game point"
                        else -> scoreBoard.text = scoreValue.toString()
                    }
                }
                else -> {
                    scoreBoard.text = scoreValue.toString()
                }
            }
        }
    }

}