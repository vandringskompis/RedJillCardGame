package com.example.blackjill

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    lateinit var play_button: TextView
    lateinit var multiplayer_button: TextView
    lateinit var rules_button: TextView
    lateinit var stats_button: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        play_button = findViewById(R.id.play_button)
        multiplayer_button = findViewById(R.id.multiplayer_button)
        rules_button = findViewById(R.id.rules_button)
        stats_button = findViewById(R.id.stats_button)

        play_button.setOnClickListener() {
            val intent = Intent(this, GameboardActivity::class.java)
            startActivity(intent)
        }

        rules_button.setOnClickListener() {
            val intent = Intent(this, RulesActivity::class.java)
            startActivity(intent)
        }


    }

}