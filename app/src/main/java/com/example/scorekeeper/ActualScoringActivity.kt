package com.example.scorekeeper

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.scorekeeper.databinding.ActivityActualScoringBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

enum class GAMESCORES(val gameName: String, val scoreRange: IntRange, val countingType: Array<String>) {
    CRICKET("Cricket", 0..300, arrayOf("standard", "sixes", "fours")),
    FOOTBALL("Football", 0..9, arrayOf("standard", "penalty")),
    BADMINTON("Badminton", 0..15, arrayOf("standard", "seven_points"))
}

class ActualScoringActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityActualScoringBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityActualScoringBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val sportsName = getSportsName()
        setUpSpinners(sportsName)
        addingPointsForTeams(sportsName, "A")
        addingPointsForTeams(sportsName, "B")
    }

    private fun setUpSpinners(sportsName: GAMESCORES) {
        val countingTypesAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            sportsName.countingType
        )
        viewBinding.typeOfCounting.adapter = countingTypesAdapter
    }

    private fun getSportsName(): GAMESCORES {
        val importedValue = intent.getStringExtra("GAME_NAME")
        val sportsName: GAMESCORES = GAMESCORES.values().find { it.name == importedValue }
            ?: GAMESCORES.BADMINTON
        viewBinding.nameOfTheGame.text = sportsName.gameName
        return sportsName
    }

    private fun addingPointsForTeams(sportsName: GAMESCORES, teamName: String) {
        val btnTeamScore: FloatingActionButton = when (teamName) {
            "A" -> viewBinding.floatingButtonTeamA
            "B" -> viewBinding.floatingButtonTeamB
            else -> throw IllegalArgumentException("Invalid team name")
        }

        val scoreBoard: TextView = when (teamName) {
            "A" -> viewBinding.teamAScore
            "B" -> viewBinding.teamBScore
            else -> throw IllegalArgumentException("Invalid team name")
        }

        var scoreValue = 0
        btnTeamScore.setOnClickListener {
           if(sportsName.countingType.toString().compareTo("standard") == 0) {
               scoreValue++
               if (!sportsName.scoreRange.contains(scoreValue)) {
                   scoreValue = 0
               }
           } else {
               Log.d("counting Type", sportsName.countingType.iterator().toString())
           }
            scoreBoard.text = scoreValue.toString()
            compareScores(sportsName)
        }
    }

    private fun compareScores(gameName: GAMESCORES) {
        val whosWinning = viewBinding.whosWinning
        val teamAScore = viewBinding.teamAScore
        val teamBScore = viewBinding.teamBScore

        val maximumScore = gameName.scoreRange.last

        when {
            teamAScore.text.toString().toInt() < teamBScore.text.toString().toInt() -> {
                whosWinning.text = "Team B seems to be winning"
                if (teamBScore.text.toString().toInt() == maximumScore) {
                    whosWinning.text = "Team B wins!"
                }
            }
            teamAScore.text.toString().toInt() > teamBScore.text.toString().toInt() -> {
                whosWinning.text = "Team A seems to be winning"
                if (teamAScore.text.toString().toInt() == maximumScore) {
                    whosWinning.text = "Team A wins"
                }
            }
            teamAScore.text.toString().toInt() == 0 && teamBScore.text.toString().toInt() == 0 -> {
                whosWinning.text = "Let's have another round then"
            }
            else -> {
                whosWinning.text = "Seems to be a draw"
            }
        }
    }
}
