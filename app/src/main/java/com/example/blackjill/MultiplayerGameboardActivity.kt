package com.example.blackjill

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible

class MultiplayerGameboardActivity : AppCompatActivity() {

    lateinit var cards: List<ImageView>
    lateinit var winnerLoseImgPlayer1: ImageView
    lateinit var winnerLoseImgPlayer2: ImageView
    lateinit var player1: Player
    lateinit var player2: Player
    lateinit var dealer: Player
    var hitCounter = 0
    var cardValues = MutableList(15) { 0 }
    var multiGameCount = 0
    var multiWinCount = 0
    var multiLostCount = 0
    var multiTieCount = 0

    lateinit var winningLogicHandler: WinningLogicHandler

    lateinit var standButton: Button
    lateinit var hitButton: Button

    lateinit var dealerScoreTextView: TextView
    lateinit var player1ScoreTextView: TextView
    lateinit var player2ScoreTextView: TextView
    lateinit var playersTurnTextView: TextView
    lateinit var playersTurnCardview: CardView

    var generateCardsFromList: List<Cards> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_multiplayer_gameboard)
        player1 = Player(1, 0)
        player2 = Player(1, 0)
        dealer = Player(0, 0)


        standButton = findViewById(R.id.stand_deal_button)
        hitButton = findViewById(R.id.hit_button)

        //Shows which players turn it is.
        playersTurnTextView = findViewById(R.id.players_turn)
        playersTurnCardview = findViewById(R.id.players_turn_cardview)

        // GameStatsHandler keeps memory of the stats.
        val stats = GameStatsHandler.getMultiGameStats(this)
        multiGameCount = stats.multiGameCount
        multiWinCount = stats.multiWinCount
        multiLostCount = stats.multiLostCount
        multiTieCount = stats.multiTieCount

        //Backside of card.
        Cards(R.drawable.card_down_o, 0)

        //Playing cards. Card 1-5 is dealers. Card 6-10 is player1. Card 11-15 is player2.
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
            findViewById(R.id.card_10),
            findViewById(R.id.card_11),
            findViewById(R.id.card_12),
            findViewById(R.id.card_13),
            findViewById(R.id.card_14),
            findViewById(R.id.card_15),
        )

        //TextViews that show current value of all visible cards
        player1ScoreTextView = findViewById(R.id.player1_score)
        player2ScoreTextView = findViewById(R.id.player2_score)
        dealerScoreTextView = findViewById(R.id.dealer_score)

        //ImageView show if it's a tie, win och lost-game.
        winnerLoseImgPlayer1 = findViewById(R.id.win_lose_img_player1)
        winnerLoseImgPlayer2 = findViewById(R.id.win_lose_img_player2)

        //Class WinningLogicHandler.
        winningLogicHandler = WinningLogicHandler(
            player1, player2, cardValues, dealer,
            winnerLoseImgPlayer1,
            winnerLoseImgPlayer2,
            playersTurnTextView,
            playersTurnCardview,
            multiWinCount,
            multiGameCount,
            multiLostCount,
            multiTieCount,
            standButton,
            hitButton, this
        )
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Exit button that will return the user to MainActivity.
        val exitButton = findViewById<TextView>(R.id.exit_button)
        exitButton.setOnClickListener() {
            finish()
        }

        // Hit button, card is dealt.

        hitButton.isEnabled = false

        hitButton.setOnClickListener() {
            hitButton.isEnabled = false
            hitCounter++

            if (player2.hitCounter == 3) {
                hitCounter = 4
                player2.hitCounter = 5
            }

            when (hitCounter) {

                1 -> {
                    cards[7].visibility = View.VISIBLE
                    cardValues[7] = generateCardsFromList[7].value
                    updateScore()
                    winningLogicHandler.checkWin()
                }

                2 -> {
                    cards[8].visibility = View.VISIBLE
                    cardValues[8] = generateCardsFromList[8].value
                    updateScore()
                    winningLogicHandler.checkWin()
                }

                3 -> {
                    cards[9].visibility = View.VISIBLE
                    cardValues[9] = generateCardsFromList[9].value
                    player1.hitCounter = 3
                    updateScore()
                    winningLogicHandler.checkWin()
                }

                4 -> {
                    cards[12].visibility = View.VISIBLE
                    cardValues[12] = generateCardsFromList[12].value
                    updateScore()
                    winningLogicHandler.checkWin()
                }

                5 -> {
                    cards[13].visibility = View.VISIBLE
                    cardValues[13] = generateCardsFromList[13].value
                    updateScore()
                    winningLogicHandler.checkWin()
                }

                6 -> {
                    cards[14].visibility = View.VISIBLE
                    cardValues[14] = generateCardsFromList[14].value
                    player2.hitCounter = 6
                    updateScore()
                    winningLogicHandler.checkWin()
                }
            }
            val handlerHit = Handler(Looper.getMainLooper())

            handlerHit.postDelayed(
                {
                    hitButton.isEnabled =
                        !(winnerLoseImgPlayer1.isVisible && winnerLoseImgPlayer2.isVisible)

                }, 1000
            )
        }

        // Stand button, player choose to stop. Dealer's cards will be dealt now.
        // Is also the dealbutton.

        standButton.setOnClickListener() {
            updateScore()

            if (winnerLoseImgPlayer2.isVisible && winnerLoseImgPlayer1.isVisible) {
                playAgain()
            }

            if (dealer.hitCounter == 0) {
                generateCards()
                updateScore()
                winningLogicHandler.checkWin()
                dealer.hitCounter++
                standButton.text = getText(R.string.stands_button)
            }

            if (player1.scoreResult > 1 && dealer.hitCounter > 1 && !winnerLoseImgPlayer2.isVisible && winnerLoseImgPlayer1.isVisible) {
                dealerPlays()

            } else if (player1.scoreResult > 1 && dealer.hitCounter > 2 && !winnerLoseImgPlayer2.isVisible && !winnerLoseImgPlayer1.isVisible) {
                dealerPlays()

            } else if (player1.scoreResult > 1 && dealer.hitCounter > 1 && winnerLoseImgPlayer2.isVisible) {
                dealerPlays()

            } else if (player1.scoreResult > 1 && dealer.hitCounter == 2 && !winnerLoseImgPlayer2.isVisible) {
                playersTurnTextView.text = getText(R.string.player2_turn)
                hitCounter = 3
                dealer.hitCounter++
            }
        }
    }

    /**
     * Turn off stand and hit button while cards are dealt and then turned on.
     * 10 cards from the card deck will be generated and the first 5(6)
     * cards will be dealt on the table.
     */
    private fun generateCards() {

        standButton.isEnabled = false
        hitButton.isEnabled = false
       winningLogicHandler.updateGameCount()
        val handler = Handler(Looper.getMainLooper())

        generateCardsFromList = CardDeck.cardList.shuffled().take(15)

        val cardIndex = listOf(0, 1, 5, 6, 10, 11)

        fun dealCards(index: Int, delay: Long) {

            handler.postDelayed({
                if (index == 1) {
                    cards[index].setImageResource(R.drawable.card_down_o)
                    cardValues[index] = 0
                } else {
                    cards[index].setImageResource(generateCardsFromList[index].cardName)
                    cardValues[index] = generateCardsFromList[index].value
                    updateScore()
                    winningLogicHandler.checkWin()
                }
                cards[index].visibility = View.VISIBLE
                updateScore()
                winningLogicHandler.checkWin()

                if (index == 11) {
                    cards[index].setImageResource(generateCardsFromList[index].cardName)
                    cardValues[index] = generateCardsFromList[index].value
                    updateScore()
                    winningLogicHandler.checkWin()

                    if (winnerLoseImgPlayer1.isVisible) {
                        playersTurnTextView.text = getText(R.string.player2_turn)
                        player2.hitCounter = 3
                        // hitCounter = 3
                    }
                    hitButton.isEnabled = true
                    dealer.hitCounter++
                    standButton.isEnabled = true
                    playersTurnCardview.visibility = View.VISIBLE
                    playersTurnTextView.visibility = View.VISIBLE
                }
            }, delay)
        }
        for (i in cardIndex.indices) {
            dealCards(cardIndex[i], (i + 1) * 1000L)
        }
        updateScore()
        winningLogicHandler.checkWin()

        val cardSecondIndex = listOf(2, 3, 4, 7, 8, 9, 12, 13, 14)
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
        val player1Cards =
            listOf(cardValues[5], cardValues[6], cardValues[7], cardValues[8], cardValues[9])
        val player2Cards =
            listOf(
                cardValues[10], cardValues[11], cardValues[12], cardValues[13], cardValues[14]
            )

        dealer.scoreResult = calculateScore(dealerCards)
        dealerScoreTextView.text = dealer.scoreResult.toString()

        player1.scoreResult = calculateScore(player1Cards)
        player1ScoreTextView.text = player1.scoreResult.toString()

        player2.scoreResult = calculateScore(player2Cards)
        player2ScoreTextView.text = player2.scoreResult.toString()
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
        winnerLoseImgPlayer1.visibility = View.INVISIBLE
        winnerLoseImgPlayer2.visibility = View.INVISIBLE
        //All cards value is set to 0
        cardValues = MutableList(15) { 0 }
        //Counters reset
        hitCounter = 0
        dealer.hitCounter = 0
        player2.hitCounter = 1

        //Score reset
        player1.scoreResult = 0
        player2.scoreResult = 0
        dealer.scoreResult = 0
        updateScore()
        playersTurnTextView.setText(R.string.player1_turn)
    }

    fun dealerPlays() {

        playersTurnTextView.setText(R.string.dealer_plays)
        dealer.hitCounter = 3
        val handler = Handler(Looper.getMainLooper())
        if (winnerLoseImgPlayer1.isVisible && winnerLoseImgPlayer2.isVisible) {
            return
        }
        standButton.isEnabled = false
        hitButton.isEnabled = false

        fun dealerPlayCards(indexDealerCards: Int, delay: Long) {

            handler.postDelayed({
                if (dealer.hitCounter == indexDealerCards + 2 && dealer.scoreResult < 17) {
                    cards[indexDealerCards].setImageResource(generateCardsFromList[indexDealerCards].cardName)
                    cardValues[indexDealerCards] = generateCardsFromList[indexDealerCards].value
                    cards[indexDealerCards].visibility = View.VISIBLE
                    updateScore()
                    winningLogicHandler.checkWin()
                    winningLogicHandler.gameOver()
                    dealer.hitCounter++

                    Log.d("!!!", " hitCounter dealer = ${dealer.hitCounter}")
                }
            }, delay)
        }
        for (i in 0 until 5) {
            dealerPlayCards(i, (i + 1) * 1000L)
        }
    }
}
