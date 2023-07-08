package com.example.scorekeeper

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.scorekeeper.databinding.ActivityActualScoringBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

enum class GAMESCORES(val gameName: String, var scoreRange: IntRange, val countingType: Array<String>) {
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

        viewBinding.typeOfCounting.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCountingType = sportsName.countingType[position]
                // Handle the selected counting type here
                Log.d("Selected Counting Type", selectedCountingType)

                when(selectedCountingType) {
                    "penalty" -> {
                        viewBinding.penaltyText.text = "Penalty"
                        viewBinding.penaltyScoreA.text = "0"
                        viewBinding.penaltyScoreB.text = "0"
                    } "seven_points" -> {
                        GAMESCORES.BADMINTON.scoreRange = 0..7
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
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

        val penaltyScore: TextView = when(teamName) {
            "A" -> viewBinding.penaltyScoreA
            "B" -> viewBinding.penaltyScoreB
            else -> throw IllegalArgumentException("Invalid team name")
        }
        var scoreValue = 0
        btnTeamScore.setOnClickListener {
           val selectedValue = viewBinding.typeOfCounting.selectedItem.toString()

            penaltyScore.text = "0"

            when(selectedValue.toString()) {
                "sixes" -> {
                    scoreValue += 6
                }
                "fours" -> {
                    scoreValue += 4
                } "penalty" -> {
                    viewBinding.penaltyText.text = "Penalty"
                    penaltyScore.text = (penaltyScore.text.toString().toInt()+1).toString()
                }
                else -> {
                    scoreValue++
                    if (!sportsName.scoreRange.contains(scoreValue)) {
                        scoreValue = 0
                    }
                }
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
