package com.example.scorekeeper

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import com.example.scorekeeper.databinding.ActivityMainBinding

/*
This is the page from where the user can choose which sports he/she wants the score board for
*/
class MainActivity : AppCompatActivity() {
    // This object connects to the layout activity_main.xml
    private lateinit var viewBinding: ActivityMainBinding

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

        viewBinding.gameCricket.setOnClickListener {
            callActualScoring(GAMESCORES.CRICKET)
        }

        viewBinding.gameFootball.setOnClickListener {
            callActualScoring(GAMESCORES.FOOTBALL)
        }

        // Set an OnCheckedChangeListener on the Switch to handle night mode
        viewBinding.isDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Activate night mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                // Activate day mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            // Recreate the activity to apply the theme changes
//            recreate()
        }
    }

    /*
    * This method is used to call the second page of this application
    * We pass the value of the game selected in putExtra method
    * */
    private fun callActualScoring(game: GAMESCORES) {
        val intent = Intent(this, ActualScoringActivity::class.java).also {
            it.putExtra("GAME_NAME", game.name)
            startActivity(it)
        }
    }
}
