package com.example.scorekeeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.scorekeeper.databinding.ActivityActualScoringBinding
import com.example.scorekeeper.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton


enum class GAMESCORES(val gameName:String, val scoreRange:IntRange, countingType: String){
    CRICKET("Cricket", 0..300, "ONLY_SIXES"),
    FOOTBALL("Football", 0..9, "PENALTY_SHOTS"),
    BADMINTON("Badminton", 0..15, "GAME_OF_SEVEN")
}


class ActualScoringActivity : AppCompatActivity() {
    private lateinit var viewBinding : ActivityActualScoringBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityActualScoringBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)


         lateinit var viewBinding : ActivityMainBinding

        //Getting the name of the sports selected
        val sportsName = getSportsName()

        addingPointsForTeams(sportsName, "A")
        addingPointsForTeams(sportsName, "B")
    }

    private fun getSportsName():GAMESCORES {
        val importedValue = intent.getStringExtra("GAME_NAME")
        Log.d("imported Value", importedValue.toString())
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
                btnTeamScore  = viewBinding.floatingButtonTeamA
                scoreBoard = viewBinding.teamAScore
            }
            "B" -> {
                btnTeamScore  = viewBinding.floatingButtonTeamB
                scoreBoard = viewBinding.teamBScore
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
        var whosWinning = viewBinding.whosWinning
        var teamAScore = viewBinding.teamAScore
        var teamBScore = viewBinding.teamAScore


        val maximumScore = (gameName.scoreRange.count() - 1)

            if(teamAScore.text.toString().compareTo(teamBScore.text.toString()) < 0) {
                whosWinning.text = "Team B seems to be winning"
            } else if(teamAScore.text.toString().compareTo(teamBScore.text.toString()) > 0) {
                whosWinning.text = "Team A seems to be winning"
            } else if((teamAScore.text.toString().compareTo("0") == 0) && (teamAScore.text.toString().compareTo("0") == 0) ) {
                whosWinning.text = "Let's have another round then"
            } else {
                whosWinning.text = "Seems to be a draw"
            }


    }
}