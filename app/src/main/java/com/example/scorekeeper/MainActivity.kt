package com.example.scorekeeper

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.scorekeeper.databinding.ActivityMainBinding

/*
This is the page from where user can choose which sports he/she wants the score board for
 */
class MainActivity : AppCompatActivity() {
    //This object connects to the layout activity_main.xml
    private lateinit var viewBinding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        /*
        * The name of the sports selected deduced from here
        * all the sports names are buttons and that is how deducing
        * which value was selected becomes easy
        * */

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

    /*
    * This method is used to call the second page of this applicaiton
    * We pass the value of the game selected in putExtra method
    * */
    private fun callActualScoring(game: GAMESCORES) {
        val intent = Intent(this, ActualScoringActivity::class.java).also {
            it.putExtra("GAME_NAME", game.name)
            startActivity(it)
        }

    }
}
