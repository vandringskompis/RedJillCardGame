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
import androidx.activity.enableEdgeToEdge
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible


class MultiplayerGameboardActivity : AppCompatActivity() {


    lateinit var cards: List<ImageView>

    lateinit var winnerLoseImgPLayer1: ImageView
    lateinit var winnerLoseImgPlayer2 :ImageView

    lateinit var player1: Player
    lateinit var player2: Player
    lateinit var dealer: Player

    var cardValues = MutableList(15) { 0 }

    var gameCount = 0
    var winCount = 0
    var lostCount = 0
    var tieCount = 0

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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Creating 2 players
        player1 = Player(1, 0)
        player2 = Player(1, 0)
        dealer = Player(0, 0)

        //Shows which players turn it is.
        playersTurnTextView = findViewById(R.id.players_turn)
        playersTurnCardview = findViewById(R.id.players_turn_cardview)


        // GameStatsHandler keeps memory of the stats.
        val stats = GameStatsHandler.getGameStats(this)
        gameCount = stats.gameCount
        winCount = stats.winCount
        lostCount = stats.lostCount
        tieCount = stats.tieCount

        //Shows who won.
        winnerLoseImgPLayer1 = findViewById(R.id.win_lose_img_player1)
        winnerLoseImgPlayer2 = findViewById(R.id.win_lose_img_player2)


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

        //Exit button that will return the user to MainActivity.
        val exitButton = findViewById<TextView>(R.id.exit_button)
        exitButton.setOnClickListener() {
            finish()
        }

        // Hit button, card is dealt.
        hitButton = findViewById(R.id.hit_button)
        hitButton.isEnabled = false
        hitButton.setOnClickListener() {

            when (player1.hitCounter) {
                1 -> {
                    cards[7].visibility = View.VISIBLE
                    cardValues[7] = generateCardsFromList[7].value
                    player1.hitCounter++
                    updateScore()
                    checkWin()

                }

                2 -> {
                    cards[8].visibility = View.VISIBLE
                    cardValues[8] = generateCardsFromList[8].value
                    player1.hitCounter++
                    updateScore()
                    checkWin()

                }

                3 -> {
                    cards[9].visibility = View.VISIBLE
                    cardValues[9] = generateCardsFromList[9].value
                    player1.hitCounter++
                    updateScore()
                    checkWin()

                }
            }

            when (player2.hitCounter) {
                2 -> {
                    cards[12].visibility = View.VISIBLE
                    cardValues[12] = generateCardsFromList[12].value
                    player2.hitCounter++
                    updateScore()
                    checkWin()

                }

                3 -> {
                    cards[13].visibility = View.VISIBLE
                    cardValues[13] = generateCardsFromList[13].value
                    player2.hitCounter++
                    updateScore()
                    checkWin()

                }

                4 -> {
                    cards[14].visibility = View.VISIBLE
                    cardValues[14] = generateCardsFromList[14].value
                    player2.hitCounter++
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

            if (player1.hitCounter > 1) {
                playersTurnTextView.text = getText(R.string.player2_turn)
                player2.hitCounter++

            }

            if (winnerLoseImgPlayer2.isVisible && winnerLoseImgPLayer1.isVisible) {
                playAgain()
            }

                dealer.hitCounter++

                if (dealer.hitCounter == 1) {

                    standButton.text = getText(R.string.stands_button)
                    generateCards()
                    updateScore()
                    checkWin()
                }

            if (player2.hitCounter > 3) {

                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    standButton.isEnabled = false
                    hitButton.isEnabled = false
                    if (dealer.hitCounter == 3 && dealer.scoreResult < 18) {
                        cards[1].setImageResource(generateCardsFromList[1].cardName)
                        cardValues[1] = generateCardsFromList[1].value
                        cards[1].visibility = View.VISIBLE
                        updateScore()
                        checkWin()
                        dealer.hitCounter++
                    }
                }, 1000)

                handler.postDelayed({
                    if (dealer.hitCounter == 4 && dealer.scoreResult < 18) {
                        cardValues[2] = generateCardsFromList[2].value
                        cards[2].visibility = View.VISIBLE
                        updateScore()
                        checkWin()
                        dealer.hitCounter++
                    }
                }, 3000)

                handler.postDelayed({

                    if (dealer.hitCounter == 5 && dealer.scoreResult < 18) {
                        cardValues[3] = generateCardsFromList[3].value
                        cards[3].visibility = View.VISIBLE
                        updateScore()
                        checkWin()
                        dealer.hitCounter++

                    }
                }, 5000)

                handler.postDelayed({
                    if (dealer.hitCounter == 6 && dealer.scoreResult < 18) {
                        cardValues[4] = generateCardsFromList[4].value
                        cards[4].visibility = View.VISIBLE
                        updateScore()
                        checkWin()
                    }
                }, 7000)
            }
        }
    }

    /**
     * Turn off stand and hit button while cards are dealt and then turned on.
     * 10 cards from the card deck will be generated and the first 3(4)
     * cards will be dealt on the table.
     */
    fun generateCards() {

        standButton.isEnabled = false
        hitButton.isEnabled = false
        val handler = Handler(Looper.getMainLooper())

        generateCardsFromList = CardDeck.cardList.shuffled().take(15)

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

        }, 4000)

        handler.postDelayed({
            cards[10].setImageResource(generateCardsFromList[10].cardName)
            cards[10].visibility = View.VISIBLE
            cardValues[10] = generateCardsFromList[10].value
            updateScore()
            checkWin()

        }, 5000)

        handler.postDelayed({
            cards[11].setImageResource(generateCardsFromList[11].cardName)
            cards[11].visibility = View.VISIBLE
            cardValues[11] = generateCardsFromList[11].value
            updateScore()
            checkWin()
            standButton.isEnabled = true

            if (winnerLoseImgPLayer1.isVisible) {
                playersTurnTextView.text = getText(R.string.player2_turn)
                player2.hitCounter++

            } else {
                hitButton.isEnabled = true
            }
            playersTurnCardview.visibility = View.VISIBLE
            playersTurnTextView.visibility = View.VISIBLE

        }, 6000)


        cards[2].setImageResource(generateCardsFromList[2].cardName)
        cards[3].setImageResource(generateCardsFromList[3].cardName)
        cards[4].setImageResource(generateCardsFromList[4].cardName)
        cards[7].setImageResource(generateCardsFromList[7].cardName)
        cards[8].setImageResource(generateCardsFromList[8].cardName)
        cards[9].setImageResource(generateCardsFromList[9].cardName)
        cards[10].setImageResource(generateCardsFromList[10].cardName)
        cards[11].setImageResource(generateCardsFromList[11].cardName)
        cards[12].setImageResource(generateCardsFromList[12].cardName)
        cards[13].setImageResource(generateCardsFromList[13].cardName)
        cards[14].setImageResource(generateCardsFromList[14].cardName)

    }

    /**
     * Update the TextViews with the current sum of each hand.
     */
    fun updateScore() {
        val dealerCards =
            listOf(cardValues[0], cardValues[1], cardValues[2], cardValues[3], cardValues[4])
        val player1Cards =
            listOf(cardValues[5], cardValues[6], cardValues[7], cardValues[8], cardValues[9])

        val player2Cards =
            listOf(cardValues[10], cardValues[11], cardValues[12], cardValues[13], cardValues[14])


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
    fun calculateScore(cards: List<Int>): Int {
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
    fun playAgain() {

        //Make all cards invisible
        for (card in cards) {
            card.visibility = View.INVISIBLE
        }
        //Make the img that says the player won, lost, tied, invisible.
        winnerLoseImgPLayer1.visibility = View.INVISIBLE
        winnerLoseImgPlayer2.visibility = View.INVISIBLE
        //All cards value is set to 0
        cardValues = MutableList(10) { 0 }
        //Counters reset

        player1.hitCounter = 1
        player2.hitCounter = 1

        dealer.hitCounter = 0
        //Score reset
        player1.scoreResult = 0
        player2.scoreResult = 0
        dealer.scoreResult = 0
        updateScore()
    }

    /**
     * All combinations to win, lose or tie.
     */
    fun checkWin() {

        if (player1.scoreResult == 21 && cardValues[5] + cardValues[6] == 21) {

            winning(player1)

            //Charlie-rule
        } else if (player2.scoreResult == 21 && cardValues[10] + cardValues[11] == 21) {

            winning(player2)

        } else if (player1.hitCounter == 4 && player1.scoreResult < 21) {
            winning(player1)

        } else if (player2.hitCounter == 4 && player2.scoreResult < 21) {
            winning(player1)

        } else if (player1.scoreResult > 21) {
            loosing(player1)

        } else if (player2.scoreResult > 21) {
            loosing(player2)

        } else if (dealer.scoreResult > 21) {
            winning(dealer)

        } else if (player1.scoreResult < dealer.scoreResult && dealer.scoreResult > 17) {

            loosing(player1)
        } else if (player2.scoreResult < dealer.scoreResult && dealer.scoreResult > 17) {

            loosing(player2)

        } else if (player1.scoreResult > dealer.scoreResult && dealer.scoreResult > 17) {
            winning(player1)

        } else if (player1.scoreResult == dealer.scoreResult && dealer.scoreResult > 17) {
            tie()
        } else if (player2.scoreResult == dealer.scoreResult && dealer.scoreResult > 17) {
            tie()

        } else {
            return
        }
    }

    /**
     * What happens if checkWin() finds a winning game..
     */
    fun winning(player: Player) {

        if (player == player1) {
            playersTurnTextView.text = getText(R.string.player2_turn)
            player2.hitCounter++
            winnerLoseImgPLayer1.setImageResource(R.drawable.you_win)
            winnerLoseImgPLayer1.visibility = View.VISIBLE
            player2.hitCounter++
            winCount++

        } else if (player == player2){

            winnerLoseImgPlayer2.setImageResource(R.drawable.you_win)
            winnerLoseImgPlayer2.visibility = View.VISIBLE
            winCount++

        }


        gameCount++
        GameStatsHandler.saveGameStats(this, gameCount, winCount, lostCount, tieCount)
        standButton.isEnabled = true
        hitButton.isEnabled = false
        standButton.text = getString(R.string.deal_button)

    }

    fun tie() {
        winnerLoseImg.setImageResource(R.drawable.push)
        tieCount++
        gameCount++
        GameStatsHandler.saveGameStats(this, gameCount, winCount, lostCount, tieCount)
        winnerLoseImg.visibility = View.VISIBLE
        standButton.isEnabled = true
        hitButton.isEnabled = false
        standButton.text = getString(R.string.deal_button)
    }

    /**
     * What happens if checkWin() finds a loosing game.
     */
    fun loosing(player: Player) {
        winnerLoseImg.setImageResource(R.drawable.you_lose)
        winnerLoseImg.visibility = View.VISIBLE
        lostCount++
        gameCount++
        GameStatsHandler.saveGameStats(this, gameCount, winCount, lostCount, tieCount)
        standButton.isEnabled = true
        hitButton.isEnabled = false
        standButton.text = getString(R.string.deal_button)
    }
}