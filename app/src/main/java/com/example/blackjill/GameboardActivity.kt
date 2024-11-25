package com.example.blackjill

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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

    lateinit var cards: List<ImageView>
    lateinit var winnerLoseImg: ImageView
    var dealerScoreResult = 0
    var playerScoreResult = 0
    var hitCounter = 1
    var dealerCount = 0

    var cardValues = MutableList(10) { 0 }

    var gameCount = 0
    var winCount = 0
    var lostCount = 0
    var tieCount = 0

    lateinit var standButton: Button
    lateinit var hitButton: Button

    lateinit var dealerScore: TextView
    lateinit var playerScore: TextView
    var generateCardsFromList: List<Cards> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gameboard)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val stats = GameStatsHandler.getGameStats(this)
        gameCount = stats.gameCount
        winCount = stats.winCount
        lostCount = stats.lostCount
        tieCount = stats.tieCount

        winnerLoseImg = findViewById(R.id.win_lose_img)

        //Backside of card.
        Cards(R.drawable.card_down_o, 0)

        //Playing cards. Card 1-5 is dealers. Card 6-10 is players.
        cards = listOf(
            findViewById(R.id.card_1),
            findViewById(R.id.card_2),
            findViewById(R.id.card_3),
            findViewById(R.id.card_4),
            findViewById(R.id.card_5),
            findViewById(R.id.card_6),
            findViewById(R.id.card_7),
            findViewById(R.id.card_8),
            findViewById(R.id.card_9),
            findViewById(R.id.card_10)
        )

        //TextViews that show current value of all visible cards
        playerScore = findViewById(R.id.player_score)
        dealerScore = findViewById(R.id.dealer_score)

        //Exit button that will return the user to MainActivity.
        val exitButton = findViewById<TextView>(R.id.exit_button)
        exitButton.setOnClickListener() {
            finish()
        }

        // Hit button, card is dealt.
        hitButton = findViewById(R.id.hit_button)
        hitButton.isEnabled = false
        hitButton.setOnClickListener() {
            hitButton.isEnabled = true

            when (hitCounter) {
                1 -> {
                    cards[7].visibility = View.VISIBLE
                    cardValues[7] = generateCardsFromList[7].value
                    hitCounter++
                    updateScore()
                    checkWin()
                }

                2 -> {
                    cards[8].visibility = View.VISIBLE
                    cardValues[8] = generateCardsFromList[8].value
                    hitCounter++
                    updateScore()
                    checkWin()
                }

                3 -> {
                    cards[9].visibility = View.VISIBLE
                    cardValues[9] = generateCardsFromList[9].value
                    updateScore()
                    checkWin()
                }
            }
        }

        // Stand button, player choose to stop. Dealer's cards will be dealt now.
        // Is also the dealbutton.
        standButton = findViewById(R.id.stand_deal_button)
        standButton.setOnClickListener() {
            updateScore()

            if (winnerLoseImg.isVisible) {
                playAgain()
            }
            dealerCount++

            if (dealerCount == 1) {
                standButton.setText("Stand")
                generateCards()
                updateScore()
                checkWin()
            }

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                standButton.isEnabled = false
                hitButton.isEnabled = false
                if (dealerCount == 2 && dealerScoreResult < 18) {
                    cards[1].setImageResource(generateCardsFromList[1].cardName)
                    cardValues[1] = generateCardsFromList[1].value
                    cards[1].visibility = View.VISIBLE
                    updateScore()
                    checkWin()
                    dealerCount++
                }
            }, 1000)

            handler.postDelayed({
                if (dealerCount == 3 && dealerScoreResult < 18) {
                    cardValues[2] = generateCardsFromList[2].value
                    cards[2].visibility = View.VISIBLE
                    updateScore()
                    checkWin()
                    dealerCount++
                }
            }, 3000)

            handler.postDelayed({

                if (dealerCount == 4 && dealerScoreResult < 18) {
                    cardValues[3] = generateCardsFromList[3].value
                    cards[3].visibility = View.VISIBLE
                    updateScore()
                    checkWin()
                    dealerCount++

                }
            }, 5000)

            handler.postDelayed({
                if (dealerCount == 5 && dealerScoreResult < 18) {
                    cardValues[4] = generateCardsFromList[4].value
                    cards[4].visibility = View.VISIBLE
                    updateScore()
                    checkWin()
                }
            }, 7000)
        }
    }

    /**
     * Turn of stand and hit button while cards are dealt and then turned on.
     * 10 cards from the card deck will be generated and the first 3(4)
     * cards will be dealt on the table.
     */
    fun generateCards() {

        standButton.isEnabled = false
        hitButton.isEnabled = false
        val handler = Handler(Looper.getMainLooper())

        generateCardsFromList = CardDeck.cardList.shuffled().take(10)

        handler.postDelayed({
            cards[0].setImageResource(generateCardsFromList[0].cardName)
            cards[0].visibility = View.VISIBLE
            cardValues[0] = generateCardsFromList[0].value
            updateScore()
            checkWin()
        }, 1000)

        handler.postDelayed({
            cards[1].setImageResource(R.drawable.card_down_o)
            cards[1].visibility = View.VISIBLE
            updateScore()
            checkWin()
        }, 2000)

        handler.postDelayed({
            cards[5].setImageResource(generateCardsFromList[5].cardName)
            cards[5].visibility = View.VISIBLE
            cardValues[5] = generateCardsFromList[5].value
            updateScore()
            checkWin()

        }, 3000)

        handler.postDelayed({
            cards[6].setImageResource(generateCardsFromList[6].cardName)
            cards[6].visibility = View.VISIBLE
            cardValues[6] = generateCardsFromList[6].value
            updateScore()
            checkWin()
            standButton.isEnabled = true
            hitButton.isEnabled = true

        }, 4000)

        cards[2].setImageResource(generateCardsFromList[2].cardName)
        cards[3].setImageResource(generateCardsFromList[3].cardName)
        cards[4].setImageResource(generateCardsFromList[4].cardName)
        cards[7].setImageResource(generateCardsFromList[7].cardName)
        cards[8].setImageResource(generateCardsFromList[8].cardName)
        cards[9].setImageResource(generateCardsFromList[9].cardName)

    }

    /**
     * Update the TextViews with the current sum of each hand.
     */
    fun updateScore() {
        val dealerCards =
            listOf(cardValues[0], cardValues[1], cardValues[2], cardValues[3], cardValues[4])
        val playerCards =
            listOf(cardValues[5], cardValues[6], cardValues[7], cardValues[8], cardValues[9])

        dealerScoreResult = calculateScore(dealerCards)
        dealerScore.text = dealerScoreResult.toString()

        playerScoreResult = calculateScore(playerCards)
        playerScore.text = playerScoreResult.toString()
    }

    /**
     *  Calculate the score and might recalculate ace's value
     */
    fun calculateScore(cards: List<Int>): Int {
        var score = cards.sum()
        var aces = cards.count { it == 11 }

        while (score > 21 && aces > 0) {
            score -= 10
            aces -= 1
        }
        return score
    }

    /**
     * Reset the game board
     */
    fun playAgain() {

        //Make all cards invisible
        for (card in cards) {
            card.visibility = View.INVISIBLE
        }
        //Make the img that says the player won, lost, tied, invisible.
        winnerLoseImg.visibility = View.INVISIBLE
        //All cards value is set to 0
        cardValues = MutableList(10) { 0 }
        //Counters reset
        hitCounter = 1
        dealerCount = 0
        //Score reset
        playerScoreResult = 0
        dealerScoreResult = 0
        updateScore()
    }

    /**
     * All combinations to win, lose or tie.
     */
    fun checkWin() {

        if (playerScoreResult == 21 && cardValues[5] + cardValues[6] == 21) {

            winning()

        } else if (playerScoreResult > 21) {
            loosing()

        } else if (dealerScoreResult > 21) {
            winning()

        } else if (playerScoreResult < dealerScoreResult && dealerScoreResult > 17) {

            loosing()

        } else if (playerScoreResult > dealerScoreResult && dealerScoreResult > 17 || dealerCount == 5) {
            winning()

        } else if (playerScoreResult == dealerScoreResult && dealerScoreResult > 17) {
            winnerLoseImg.setImageResource(R.drawable.push)
            tieCount++
            gameCount++
            GameStatsHandler.saveGameStats(this, gameCount, winCount, lostCount, tieCount)
            winnerLoseImg.visibility = View.VISIBLE
            standButton.isEnabled = true
            hitButton.isEnabled = false
            standButton.setText("Deal")
        } else {
            return
        }
    }

    /**
     * What happens if checkWin() finds a winning game..
     */
    fun winning() {
        winnerLoseImg.setImageResource(R.drawable.you_win)
        winnerLoseImg.visibility = View.VISIBLE
        winCount++
        gameCount++
        GameStatsHandler.saveGameStats(this, gameCount, winCount, lostCount, tieCount)
        standButton.isEnabled = true
        hitButton.isEnabled = false
        standButton.setText("Deal")
    }

    /**
     * What happens if checkWin() finds a loosing game.
     */
    fun loosing() {
        winnerLoseImg.setImageResource(R.drawable.you_lose)
        winnerLoseImg.visibility = View.VISIBLE
        lostCount++
        gameCount++
        GameStatsHandler.saveGameStats(this, gameCount, winCount, lostCount, tieCount)
        standButton.isEnabled = true
        hitButton.isEnabled = false
        standButton.setText("Deal")
    }
}