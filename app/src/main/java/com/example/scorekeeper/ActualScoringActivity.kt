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

/*
* This enum is used in both pages of the app
* it containes the sports name, score range which is changable
* and the types of counting method available
*  */
enum class GAMESCORES(val gameName: String, var scoreRange: IntRange, val countingType: Array<String>) {
    CRICKET("Cricket", 0..300, arrayOf("standard", "sixes", "fours")),
    FOOTBALL("Football", 0..9, arrayOf("standard", "penalty")),
    BADMINTON("Badminton", 0..15, arrayOf("standard", "seven_points"))
}

/*
* This is the method that implements the scoring logic
* it is divided into three methids
*       setUpSpinners(sportsName)
        addingPointsForTeams(sportsName, "A")
        addingPointsForTeams(sportsName, "B")
* */
class ActualScoringActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityActualScoringBinding
    private var penaltyScoreCalculator = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityActualScoringBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        /*
        * This method helps us deduce the sports name according
        * to the value passeed by the previous page
        * */
        val sportsName = getSportsName()

        // Sets up the value of the dropdown according to the sports selected
        setUpSpinners(sportsName)
        //One single method is called to set the values of two seperate teams
        addingPointsForTeams(sportsName, "A")
        addingPointsForTeams(sportsName, "B")
    }

    private fun setUpSpinners(sportsName: GAMESCORES) {
        //setting up the value according to the sports selected
        val countingTypesAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            sportsName.countingType
        )
        viewBinding.typeOfCounting.adapter = countingTypesAdapter

        //Setting up default values (if needed) for the dropdown value selected
        viewBinding.typeOfCounting.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCountingType = sportsName.countingType[position]
                // Handle the selected counting type here
                Log.d("Selected Counting Type", selectedCountingType)

                when(selectedCountingType) {
                    "penalty" -> {
                        penaltyScoreCalculator = 0
                        viewBinding.penaltyText.text = "Penalty"
                        viewBinding.penaltyScoreA.text = penaltyScoreCalculator.toString()
                        viewBinding.penaltyScoreB.text = penaltyScoreCalculator.toString()
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


    /*
    * gets the name from the previous page and displays
    * it on the new page also returns the value to
    * be used for the operations
    * */
    private fun getSportsName(): GAMESCORES {
        val importedValue = intent.getStringExtra("GAME_NAME")
        val sportsName: GAMESCORES = GAMESCORES.values().find { it.name == importedValue }
            ?: GAMESCORES.BADMINTON
        viewBinding.nameOfTheGame.text = sportsName.gameName
        return sportsName
    }

    /*
    *
    * This vlaue is used to impliment the operation of scoring
    * it firstly sets up the value according to the team selected
    * and then the onclick method does the actual calculations
    * based on the type of counting selected
    * */
    private fun addingPointsForTeams(sportsName: GAMESCORES, teamName: String) {

        //Setting up the btnFloat according to the team we need to add score to
        val btnTeamScore: FloatingActionButton = when (teamName) {
            "A" -> viewBinding.floatingButtonTeamA
            "B" -> viewBinding.floatingButtonTeamB
            else -> throw IllegalArgumentException("Invalid team name")
        }

        //Setting up the scoreboard according to the team we need to add score to
        val scoreBoard: TextView = when (teamName) {
            "A" -> viewBinding.teamAScore
            "B" -> viewBinding.teamBScore
            else -> throw IllegalArgumentException("Invalid team name")
        }
        //Setting up the penalty (for football) according to the team we need to add score to
        val penaltyScore: TextView = when(teamName) {
            "A" -> viewBinding.penaltyScoreA
            "B" -> viewBinding.penaltyScoreB
            else -> throw IllegalArgumentException("Invalid team name")
        }
        var scoreValue = 0

        /*
            All sports have different scoring systems
            1. Badminton has either a 7 points game or a 15 points game (seven_points, standard)
            2. Cricket a player can hit 6's and 4's so it can be calculated
               faster if we had options (standard, sixes, fours)
            3. In football if there is a penalty then the number of penalty goals
               start from 0 and the score is kept seprate from it (penalty)
            4. Starad counting is normally adding one value at each point (else)
               This method is called on plus sign being clicked and
               it operates based on the drop down value selcted
         */
        btnTeamScore.setOnClickListener {
           val selectedValue = viewBinding.typeOfCounting.selectedItem.toString()

            when(selectedValue.toString()) {
                "sixes" -> {
                    scoreValue += 6
                }
                "fours" -> {
                    scoreValue += 4
                } "penalty" -> {
                    penaltyScore.text = (penaltyScoreCalculator+1).toString()
                }
                else -> {
                    scoreValue++
                    if (!sportsName.scoreRange.contains(scoreValue)) {
                        scoreValue = 0
                    }
                }
            }

            //after the different scores are calculated they are displaced using scoreBoard
            scoreBoard.text = scoreValue.toString()
            //This method compares the score and lets us know who is winning
            compareScores(sportsName)
        }
    }

    /*
    * It is called to compare the score between the two teams
    * */
    private fun compareScores(gameName: GAMESCORES) {
        val whosWinning = viewBinding.whosWinning
        val teamAScore = viewBinding.teamAScore
        val teamBScore = viewBinding.teamBScore

        //the team that reaches the maximum score first wins
        val maximumScore = gameName.scoreRange.last

        when {
            //Team A scores < Team B
            teamAScore.text.toString().toInt() < teamBScore.text.toString().toInt() -> {
                whosWinning.text = "Team B seems to be winning"
                //Team B has scored the maximum score that can be achieved
                if (teamBScore.text.toString().toInt() == maximumScore) {
                    whosWinning.text = "Team B wins!"
                }
            }
            //Team A Scores > Team B
            teamAScore.text.toString().toInt() > teamBScore.text.toString().toInt() -> {
                whosWinning.text = "Team A seems to be winning"
                //Team A scored the maximum score that can be achieved
                if (teamAScore.text.toString().toInt() == maximumScore) {
                    whosWinning.text = "Team A wins"
                }
            }
            //If the game restarts
            teamAScore.text.toString().toInt() == 0 && teamBScore.text.toString().toInt() == 0 -> {
                whosWinning.text = "Let's have another round then"
            }
            else -> {
                //If both teams have the same score
                whosWinning.text = "Seems to be a draw"
            }
        }
    }
}
