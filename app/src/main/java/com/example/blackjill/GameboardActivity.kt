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
        // GameStatsHandler keeps memory of the stats.
        val stats = GameStatsHandler.getGameStats(this)
        gameCount = stats.gameCount
        winCount = stats.winCount
        lostCount = stats.lostCount
        tieCount = stats.tieCount

        //ImageView that show up to announce the winner/tie.
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
            hitButton.isEnabled = false

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
                    hitCounter++
                    updateScore()
                    checkWin()
                }
            }

            val handlerHit = Handler(Looper.getMainLooper())

            handlerHit.postDelayed({

                hitButton.isEnabled = !winnerLoseImg.isVisible

            },1000
            )
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
                standButton.text = getText(R.string.stands_button)
                generateCards()
                updateScore()
                checkWin()
            }

            if (dealerCount == 2 && dealerScoreResult < 18) {
                dealerPlays()
            }
        }
    }

    /**
     * Turn off stand and hit button while cards are dealt and then turned on.
     * 10 cards from the card deck will be generated and the first 3(4)
     * cards will be dealt on the table.
     */
    private fun generateCards() {

        standButton.isEnabled = false
        hitButton.isEnabled = false
        val handler = Handler(Looper.getMainLooper())

        generateCardsFromList = CardDeck.cardList.shuffled().take(10)

        val cardIndex = listOf(0, 1, 5, 6)

        fun dealCards(index : Int, delay : Long){

            handler.postDelayed({
                if (index == 1){
                    cards[index].setImageResource(R.drawable.card_down_o)
                    cardValues[index] = 0
                }else {
                    cards[index].setImageResource(generateCardsFromList[index].cardName)
                    cardValues[index] = generateCardsFromList[index].value
                }

                cards[index].visibility = View.VISIBLE
                updateScore()
                checkWin()

                if (index == 6) {
                    hitButton.isEnabled = !winnerLoseImg.isVisible
                    standButton.isEnabled = true
                }
            }, delay)
        }
        for (i in cardIndex.indices) {
            dealCards(cardIndex[i], (i + 1) * 1000L )
        }

        val cardSecondIndex = listOf(2, 3, 4, 7, 8, 9)
        for (index in cardSecondIndex) {
            cards[index].setImageResource(generateCardsFromList[index].cardName)
        }
    }

    /**
     * Update the TextViews with the current sum of each hand.
     */
    private fun updateScore() {
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
    private fun calculateScore(cards: List<Int>): Int {
        var scoreTotal = cards.sum()
        var acesCount = cards.count { it == 11 }

        while (scoreTotal > 21 && acesCount > 0) {
            scoreTotal -= 10
            acesCount -= 1
        }
        return scoreTotal
    }

    /**
     * Reset the game board
     */
    private fun playAgain() {

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
    private fun checkWin() {

        if (playerScoreResult == 21 && cardValues[5] + cardValues[6] == 21) {
            winning()

            //Charlie-rule
        } else if (hitCounter == 4 && playerScoreResult < 21) {
            winning()

        } else if (playerScoreResult > 21) {
            loosing()

        } else if (dealerScoreResult > 21) {
            winning()

        } else if (playerScoreResult < dealerScoreResult && dealerScoreResult > 17) {
            loosing()

        } else if (playerScoreResult > dealerScoreResult && dealerScoreResult > 17) {
            winning()

        } else if (playerScoreResult == dealerScoreResult && dealerScoreResult > 17) {
            winnerLoseImg.setImageResource(R.drawable.push)
            tieCount++
            gameOver()
        } else {
            return
        }
    }

    private fun gameOver() {
        gameCount++
        GameStatsHandler.saveGameStats(this, gameCount, winCount, lostCount, tieCount)
        winnerLoseImg.visibility = View.VISIBLE
        standButton.isEnabled = true
        hitButton.isEnabled = false
        standButton.text = getString(R.string.deal_button)
    }

    /**
     * What happens if checkWin() finds a winning game..
     */
    private fun winning() {
        winnerLoseImg.setImageResource(R.drawable.you_win)
        winCount++
        gameOver()
    }

    /**
     * What happens if checkWin() finds a loosing game.
     */
    private fun loosing() {
        winnerLoseImg.setImageResource(R.drawable.you_lose)
        lostCount++
        gameOver()
    }

    private fun dealerPlays() {
        val handler = Handler(Looper.getMainLooper())

        standButton.isEnabled = false
        hitButton.isEnabled = false

        fun dealerPlaysCards(indexDealerCards: Int, delay: Long) {

            handler.postDelayed({
                if (dealerScoreResult < 18) {
                    cards[indexDealerCards].setImageResource(generateCardsFromList[indexDealerCards].cardName)
                    cardValues[indexDealerCards] = generateCardsFromList[indexDealerCards].value
                    cards[indexDealerCards].visibility = View.VISIBLE
                    updateScore()
                    checkWin()
                    dealerCount++
                }
            }, delay)
        }
        for (i in 1 until 4) {
            dealerPlaysCards(i, i * 1000L)
        }

    }
}
