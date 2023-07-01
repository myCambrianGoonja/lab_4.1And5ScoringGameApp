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


enum class GAMESCORES(val gameName:String, val scoreRange:IntRange){
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

    private fun getSportsName():GAMESCORES {
        val importedValue = intent.getStringExtra("GAME_NAME").toString()
        val sportsName: GAMESCORES = GAMESCORES.values().find { it.name == importedValue }?: GAMESCORES.BADMINTON
        var textViewName = findViewById<TextView>(R.id.nameOfTheGame).apply {
            text = sportsName.name
        }
        return sportsName
    }


    private fun addingPointsForTeams(sportsName: GAMESCORES, teamName: String) {
        lateinit var btnTeamScore:FloatingActionButton
        lateinit var scoreBoard:TextView
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
        btnTeamScore.setOnClickListener {
            scoreValue++
            if (!sportsName.scoreRange.contains(scoreValue)) {
                scoreValue = 0
            }

            scoreBoard.text = scoreValue.toString()
            compareScores(sportsName, scoreValue)
        }
    }

    fun compareScores(gameName: GAMESCORES, scoreValue: Int) {
        var whosWinning = findViewById<TextView>(R.id.whosWinning)
        var teamAScore = findViewById<TextView>(R.id.teamAScore)
        var teamBScore = findViewById<TextView>(R.id.teamBScore)

        Log.d("Score team A", teamAScore.text.toString())
        Log.d("Score team B", teamBScore.text.toString())
        Log.d("see the value", (teamAScore.text.toString().compareTo("0")).toString())

            if(teamAScore.text.toString().compareTo(teamBScore.text.toString()) < 0) {
                whosWinning.text = "Team B seems to be winning"
            } else if(teamAScore.text.toString().compareTo(teamBScore.text.toString()) > 0) {
                whosWinning.text = "Team A seems to be winning"
            } else if((teamAScore.text.toString().compareTo("0") == 0) && (teamAScore.text.toString().compareTo("0") == 0) ) {
                whosWinning.text = "Let's have another round then"
            } else {
                whosWinning.text = "Seems to be a draw"
            }

        when(gameName) {
            GAMESCORES.BADMINTON -> {
                if(teamAScore.text.toString().compareTo("7") == 0) {
                    whosWinning.text = "Team A won!"
                } else if(teamBScore.text.toString().compareTo("7") == 0) {
                    whosWinning.text = "Team B won!"
                }
            }
            GAMESCORES.CRICKET -> {

            }
            GAMESCORES.FOOTBALL -> {

            }
        }

    }
}