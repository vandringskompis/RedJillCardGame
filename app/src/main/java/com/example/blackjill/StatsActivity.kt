package com.example.blackjill

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class StatsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_stats)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var (gameCount, winCount, lostCount, tieCount) = getGameStats(this)

        var updateStatsText = getString(R.string.Stats_text, gameCount, winCount, lostCount, tieCount)

        findViewById<TextView>(R.id.the_stats).text = updateStatsText
    }

    fun getGameStats(context: Context) : GameStats{
        val sharedPreferences = context.getSharedPreferences("GamesStats", Context.MODE_PRIVATE)
        val gameCount = sharedPreferences.getInt("GAME_COUNT", 0)
        val winCount = sharedPreferences.getInt("WIN_COUNT", 0)
        val lostCount = sharedPreferences.getInt("LOST_COUNT", 0)
        val tieCount = sharedPreferences.getInt("TIE_COUNT", 0)

        return GameStats(gameCount, winCount, lostCount, tieCount)
    }
}