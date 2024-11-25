package com.example.blackjill

import android.content.Context
import android.os.Bundle
import android.widget.Button
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

        val stats = GameStatsHandler.getGameStats(this)

        val gameCount = stats.gameCount
        val winCount = stats.winCount
        val lostCount = stats.lostCount
        val tieCount = stats.tieCount

        val updateStatsText = getString(R.string.Stats_text, gameCount, winCount, lostCount, tieCount)

        findViewById<TextView>(R.id.the_stats).text = updateStatsText


        val backButton = findViewById<Button>(R.id.back_button_stats)

        backButton.setOnClickListener(){
            finish()
        }
    }


}