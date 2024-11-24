package com.example.blackjill

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract.CommonDataKinds.Im
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible

class GameboardActivity : AppCompatActivity() {

    lateinit var card1: ImageView
    lateinit var card2: ImageView
    lateinit var card3: ImageView
    lateinit var card4: ImageView
    lateinit var card5: ImageView
    lateinit var card6: ImageView
    lateinit var card7: ImageView
    lateinit var card8: ImageView
    lateinit var card9: ImageView
    lateinit var card10: ImageView
    var dealerScoreResult = 0
    var playerScoreResult = 0
    var card1Value = 0
    var card2Value = 0
    var card3Value = 0
    var card4Value = 0
    var card5Value = 0
    var card6Value = 0
    var card7Value = 0
    var card8Value = 0
    var card9Value = 0
    var card10Value = 0
    lateinit var dealerScore: TextView
    lateinit var playerScore: TextView
    lateinit var cardList: List<Cards>
    lateinit var generateCardsFromList: List<Cards>

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
        Cards(R.drawable.card_down_o, 0)

        cardList = listOf(
            Cards(R.drawable.ace_of_clubs, 11),
            Cards(R.drawable.ace_of_hearts, 11),
            Cards(R.drawable.ace_of_spades, 11),
            Cards(R.drawable.ace_of_diamonds, 11),
            Cards(R.drawable.two_spades, 2),
            Cards(R.drawable.three_clubs, 3),
            Cards(R.drawable.spades_three, 3),
            Cards(R.drawable.hearts_three, 3),
            Cards(R.drawable.diamonds_three, 3),
            Cards(R.drawable.four_of_hearts, 4),
            Cards(R.drawable.four_of_clubs, 4),
            Cards(R.drawable.four_of_spades, 4),
            Cards(R.drawable.four_of_diamonds, 4),
            Cards(R.drawable.five_of_spades, 5),
            Cards(R.drawable.five_of_clubs, 5),
            Cards(R.drawable.five_of_hearts, 5),
            Cards(R.drawable.five_of_diamonds, 5),
            Cards(R.drawable.six_of_hearts, 6),
            Cards(R.drawable.six_of_clubs, 6),
            Cards(R.drawable.six_of_spades, 6),
            Cards(R.drawable.six_of_diamonds, 6),
            Cards(R.drawable.seven_of_spades, 7),
            Cards(R.drawable.seven_of_clubs, 7),
            Cards(R.drawable.seven_of_hearts, 7),
            Cards(R.drawable.seven_of_diamonds, 7),
            Cards(R.drawable.eight_of_hearts, 8),
            Cards(R.drawable.eight_of_clubs, 8),
            Cards(R.drawable.eight_of_spades, 8),
            Cards(R.drawable.eight_of_diamonds, 8),
            Cards(R.drawable.nine_of_spades, 9),
            Cards(R.drawable.nine_of_clubs, 9),
            Cards(R.drawable.nine_of_hearts, 9),
            Cards(R.drawable.nine_of_diamonds, 9),
            Cards(R.drawable.ten_of_hearts, 10),
            Cards(R.drawable.ten_of_clubs, 10),
            Cards(R.drawable.ten_of_spades, 10),
            Cards(R.drawable.ten_of_diamonds, 10),
            Cards(R.drawable.jack_of_spades, 10),
            Cards(R.drawable.jack_of_clubs, 10),
            Cards(R.drawable.jack_of_hearts, 10),
            Cards(R.drawable.jack_of_diamonds, 10),
            Cards(R.drawable.queen_of_hearts, 10),
            Cards(R.drawable.queen_of_clubs, 10),
            Cards(R.drawable.queen_of_spades, 10),
            Cards(R.drawable.queen_of_diamonds, 10),
            Cards(R.drawable.king_of_spades, 10),
            Cards(R.drawable.king_of_clubs, 10),
            Cards(R.drawable.king_of_hearts, 10),
            Cards(R.drawable.king_of_diamonds, 10)
        )


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

        //TODO if the player get a score of 21 with the first 2 cards,
        // it's Red Jill and the player wins, fix check after generateCards()
        generateCards()

        var hitCounter = 1
        updateScore()


        //Exit button that vill return the user to MainActivity.

        //TODO make fragment for exit button "are you sure you want to quit".
        val exitButton = findViewById<TextView>(R.id.exit_button)
        exitButton.setOnClickListener() {
            finish()
        }

        // Hit button, card is dealt.
        val hitButton = findViewById<Button>(R.id.hit_button)
        hitButton.setOnClickListener() {


            when (hitCounter) {
                1 -> {
                    card8.visibility = View.VISIBLE
                    card8Value = generateCardsFromList[7].value
                    hitCounter++
                    updateScore()
                }
                2 -> {
                    card9.visibility = View.VISIBLE
                    card9Value = generateCardsFromList[8].value
                    hitCounter++
                    updateScore()
                }
                3 -> {
                    card10.visibility = View.VISIBLE
                    card10Value = generateCardsFromList[9].value
                    updateScore()
                }

            }
        }

        // Stand button, player choose to stop. Dealer's cards will be dealt now.
        //TODO fragment that shows who won/tie
        val standButton = findViewById<Button>(R.id.stand_button)
        standButton.setOnClickListener() {
            hitButton.isEnabled = false
            val handler = Handler(Looper.getMainLooper())
            var dealerCount = 0

            handler.postDelayed({
                if (dealerCount == 0 && dealerScoreResult < 18) {
                    card2.setImageResource(generateCardsFromList[1].cardName)
                    card2Value = generateCardsFromList[1].value
                    card3.visibility = View.VISIBLE
                    updateScore()
                    dealerCount++

                }
            }, 1000)

            handler.postDelayed({
                if (dealerCount == 1 && dealerScoreResult < 18) {
                    card3.setImageResource(generateCardsFromList[2].cardName)
                    card3Value = generateCardsFromList[2].value
                    card4.visibility = View.VISIBLE
                    updateScore()
                    dealerCount++
                }
            }, 3000)

            handler.postDelayed({

                if (dealerCount ==2 && dealerScoreResult < 18) {
                    card4.setImageResource(generateCardsFromList[3].cardName)
                    card4Value = generateCardsFromList[3].value
                    card5.visibility = View.VISIBLE
                    updateScore()
                    dealerCount++
                }
            }, 6000)


                if (dealerCount == 3 && dealerScoreResult <18) {
                    card5.setImageResource(generateCardsFromList[4].cardName)
                    card5Value = generateCardsFromList[4].value
                    updateScore()
                }
        }

    }

    fun generateCards() {
        generateCardsFromList = cardList.shuffled().take(10)

        card1.setImageResource(generateCardsFromList[0].cardName)
        card1.visibility = View.VISIBLE
        card1Value = generateCardsFromList[0].value

        card6.setImageResource(generateCardsFromList[5].cardName)
        card6.visibility = View.VISIBLE
        card6Value = generateCardsFromList[5].value

        card7.setImageResource(generateCardsFromList[6].cardName)
        card7.visibility = View.VISIBLE
        card7Value = generateCardsFromList[5].value

        card8.setImageResource(generateCardsFromList[7].cardName)


        card9.setImageResource(generateCardsFromList[8].cardName)


        card10.setImageResource(generateCardsFromList[9].cardName)


    }


    fun updateScore() {
        val dealerCards = listOf(card1Value, card2Value, card3Value, card4Value, card5Value)
        val playerCards = listOf(card6Value, card7Value, card8Value, card9Value, card10Value)

        dealerScoreResult = calculateScore(dealerCards)
        dealerScore.text = dealerScoreResult.toString()

        playerScoreResult = calculateScore(playerCards)
        playerScore.text = playerScoreResult.toString()
    }

    fun calculateScore(cards: List<Int>): Int {
        var score = cards.sum()
        var aces = cards.count { it == 11 }

        while (score > 21 && aces > 0) {
            score -= 10
            aces -= 1
        }
        return score
    }


}