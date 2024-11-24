package com.example.blackjill

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

lateinit var card1 : ImageView
lateinit var card2 : ImageView
lateinit var card3 : ImageView
lateinit var card4 : ImageView
lateinit var card5 : ImageView
lateinit var card6 : ImageView
lateinit var card7 : ImageView
lateinit var card8 : ImageView
lateinit var card9 : ImageView
lateinit var card10 : ImageView
lateinit var dealerScore : TextView
lateinit var playerScore : TextView


class GameboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gameboard)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //List of all my cards

        val cardList = listOf(R.drawable.ace_of_clubs,R.drawable.ace_of_hearts, R.drawable.ace_of_spades,
            R.drawable.ace_of_diamonds, R.drawable.two_spades, R.drawable.three_clubs, R.drawable.spades_three,
            R.drawable.hearts_three, R.drawable.diamonds_three, R.drawable.four_of_hearts, R.drawable.four_of_clubs,
            R.drawable.four_of_spades, R.drawable.four_of_diamonds, R.drawable.five_of_spades, R.drawable.five_of_clubs,
            R.drawable.five_of_hearts, R.drawable.five_of_diamonds, R.drawable.six_of_hearts, R.drawable.six_of_clubs,
            R.drawable.six_of_spades, R.drawable.six_of_diamonds, R.drawable.seven_of_spades, R.drawable.seven_of_clubs,
            R.drawable.seven_of_hearts, R.drawable.seven_of_diamonds, R.drawable.eight_of_hearts, R.drawable.eight_of_clubs,
            R.drawable.eight_of_spades, R.drawable.eight_of_diamonds, R.drawable.nine_of_spades, R.drawable.nine_of_clubs,
            R.drawable.nine_of_hearts, R.drawable.nine_of_diamonds, R.drawable.ten_of_hearts, R.drawable.ten_of_clubs,
            R.drawable.ten_of_spades, R.drawable.ten_of_diamonds, R.drawable.jack_of_spades, R.drawable.jack_of_clubs,
            R.drawable.jack_of_hearts, R.drawable.jack_of_diamonds, R.drawable.queen_of_hearts, R.drawable.queen_of_clubs,
            R.drawable.queen_of_spades, R.drawable.queen_of_diamonds, R.drawable.king_of_spades, R.drawable.king_of_clubs,
            R.drawable.king_of_hearts, R.drawable.king_of_diamonds)


        //Playing cards. Card 1-5 is dealers. Card 6-10 is players.
        card1 = findViewById(R.id.card_1)
        card2 = findViewById(R.id.card_2)
        card3 = findViewById(R.id.card_3)
        card4 = findViewById(R.id.card_4)
        card5 = findViewById(R.id.card_5)
        card6 = findViewById(R.id.card_6)
        card7 = findViewById(R.id.card_7)
        card8 = findViewById(R.id.card_8)
        card9 = findViewById(R.id.card_9)
        card10 = findViewById(R.id.card_10)

        playerScore = findViewById(R.id.player_score)
        dealerScore = findViewById(R.id.dealer_score)

        //Exit button that vill return the user to MainActivity.

       //TODO make fragment for exit button "are you sure you want to quit".
        val exitButton = findViewById<TextView>(R.id.exit_button)
        exitButton.setOnClickListener() {
            finish()
        }

        // Hit button, card is dealt.
        val hitButton = findViewById<Button>(R.id.hit_button)
        hitButton.setOnClickListener(){

        }

        // Stand button, player choose to stop.
        val standButton = findViewById<Button>(R.id.stand_button)
        standButton.setOnClickListener(){

        }



    }
}