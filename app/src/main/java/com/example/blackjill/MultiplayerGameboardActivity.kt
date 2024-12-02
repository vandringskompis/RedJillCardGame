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

    lateinit var currentPlayer: Player

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
        winnerLoseImgPlayer1 = findViewById(R.id.win_lose_img_player1)
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

            hitCounter++

            when (hitCounter) {

                1 -> {
                    cards[7].visibility = View.VISIBLE
                    cardValues[7] = generateCardsFromList[7].value
                    updateScore()
                    checkWin()

                    Log.d(
                        "!!!",
                        "1 player 1 plays ${player1.hitCounter}  player 2${player2.hitCounter} $winCount $lostCount $gameCount "
                    )
                }

                2 -> {
                    cards[8].visibility = View.VISIBLE
                    cardValues[8] = generateCardsFromList[8].value
                    updateScore()
                    checkWin()
                    Log.d(
                        "!!!",
                        "2  player 1 plays ${player1.hitCounter} player 2 ${player2.hitCounter}  "
                    )

                }

                3 -> {
                    cards[9].visibility = View.VISIBLE
                    cardValues[9] = generateCardsFromList[9].value
                    player1.hitCounter = 3
                    updateScore()
                    checkWin()
                    Log.d(
                        "!!!",
                        "3 player 1 plays ${player1.hitCounter} player2${player2.hitCounter} "
                    )

                }

                4 -> {
                    cards[12].visibility = View.VISIBLE
                    cardValues[12] = generateCardsFromList[12].value
                    updateScore()
                    checkWin()
                    Log.d("!!!", "4 player1 ${player1.hitCounter} player 2${player2.hitCounter}")

                }

                5 -> {
                    cards[13].visibility = View.VISIBLE
                    cardValues[13] = generateCardsFromList[13].value
                    updateScore()
                    checkWin()
                    Log.d("!!!", "5. player1${player1.hitCounter} player2 ${player2.hitCounter}  ")

                }

                6 -> {
                    cards[14].visibility = View.VISIBLE
                    cardValues[14] = generateCardsFromList[14].value
                    player2.hitCounter = 6

                    updateScore()
                    checkWin()
                    Log.d("!!!", "6 player1 ${player1.hitCounter} pplayer2 ${player2.hitCounter} ")

                }

            }

        }

        // Stand button, player choose to stop. Dealer's cards will be dealt now.
        // Is also the dealbutton.
        standButton = findViewById(R.id.stand_deal_button)
        standButton.setOnClickListener() {
            updateScore()

            Log.d(
                "!!!",
                "1 dealerCount =  ${dealer.hitCounter}"
            )
            if (winnerLoseImgPlayer2.isVisible && winnerLoseImgPlayer1.isVisible) {
                Log.d(
                    "!!!",
                    "8 if IMG is visible for both players player1 ${player1.hitCounter} player2 ${player2.hitCounter}  "
                )
                Log.d(
                    "!!!",
                    "4 dealerCount =  ${dealer.hitCounter}"
                )
                playAgain()
            }

            if (dealer.hitCounter == 0) {
                Log.d(
                    "!!!",
                    "2 dealerCount =  ${dealer.hitCounter}"
                )
                generateCards()
                updateScore()
                checkWin()
                Log.d("!!!", "7  lets start ${player1.hitCounter} ${player2.hitCounter} ")
                dealer.hitCounter++
                standButton.text = getText(R.string.stands_button)
                Log.d(
                    "!!!",
                    "3 dealerCount =  ${dealer.hitCounter}"
                )
            }



            if (player1.scoreResult > 1 && dealer.hitCounter > 1 && !winnerLoseImgPlayer2.isVisible && winnerLoseImgPlayer1.isVisible) {
                Log.d(
                    "!!!",
                    "4 dealerCount =  ${dealer.hitCounter}"
                )
                dealerPlays()


            } else if (player1.scoreResult > 1 && dealer.hitCounter > 2 && !winnerLoseImgPlayer2.isVisible && !winnerLoseImgPlayer1.isVisible) {
                Log.d(
                    "!!!",
                    "5 dealerCount =  ${dealer.hitCounter}"
                )
                dealerPlays()


            } else if (player1.scoreResult > 1 && dealer.hitCounter > 1 && winnerLoseImgPlayer2.isVisible) {
                Log.d(
                    "!!!",
                    "6 dealerCount =  ${dealer.hitCounter}"
                )
                dealerPlays()


            } else if (player1.scoreResult > 1 && dealer.hitCounter == 2 && !winnerLoseImgPlayer2.isVisible) {
                Log.d(
                    "!!!",
                    "7 dealerCount =  ${dealer.hitCounter}"
                )
                playersTurnTextView.text = getText(R.string.player2_turn)
                hitCounter = 3
                dealer.hitCounter++
                Log.d("!!!", "14 switch players ")
                Log.d(
                    "!!!",
                    "70 dealerplays()=  ${dealer.hitCounter}"
                )

            }
        }
    }

    /**
     * Turn off stand and hit button while cards are dealt and then turned on.
     * 10 cards from the card deck will be generated and the first 3(4)
     * cards will be dealt on the table.
     */
    fun generateCards() {
        Log.d("UpdateScore", "cardValues size: ${cardValues.size}")
        standButton.isEnabled = false
        hitButton.isEnabled = false
        val handler = Handler(Looper.getMainLooper())

        Log.d(
            "!!!",
            " 10 dealerCount =  ${dealer.hitCounter}"
        )
        generateCardsFromList = CardDeck.cardList.shuffled().take(15)

        handler.postDelayed({
            runOnUiThread {
                cards[0].setImageResource(generateCardsFromList[0].cardName)
                cards[0].visibility = View.VISIBLE
                cardValues[0] = generateCardsFromList[0].value
                updateScore()
                checkWin()
            }
        }, 1000)

        handler.postDelayed({
            runOnUiThread {
                cards[1].setImageResource(R.drawable.card_down_o)
                cards[1].visibility = View.VISIBLE
                updateScore()
                checkWin()
            }
        }, 2000)

        handler.postDelayed({
            runOnUiThread {
                cards[5].setImageResource(generateCardsFromList[5].cardName)
                cards[5].visibility = View.VISIBLE
                cardValues[5] = generateCardsFromList[5].value
                updateScore()
            }
        }, 3000)

        handler.postDelayed({
            runOnUiThread {
                cards[6].setImageResource(generateCardsFromList[6].cardName)
                cards[6].visibility = View.VISIBLE
                cardValues[6] = generateCardsFromList[6].value
                updateScore()
                checkWin()
            }
        }, 4000)

        handler.postDelayed({
            runOnUiThread {
                cards[10].setImageResource(generateCardsFromList[10].cardName)
                cards[10].visibility = View.VISIBLE
                cardValues[10] = generateCardsFromList[10].value
                updateScore()

            }
        }, 5000)

        handler.postDelayed({
            runOnUiThread {
                cards[11].setImageResource(generateCardsFromList[11].cardName)
                cards[11].visibility = View.VISIBLE
                cardValues[11] = generateCardsFromList[11].value
                updateScore()
                checkWin()
                standButton.isEnabled = true
                dealer.hitCounter++

                Log.d(
                    "!!!",
                    "11 dealerCount =  ${dealer.hitCounter}"
                )

                if (winnerLoseImgPlayer1.isVisible) {
                    playersTurnTextView.text = getText(R.string.player2_turn)
                    hitCounter = 3
                }
                hitButton.isEnabled = true

                playersTurnCardview.visibility = View.VISIBLE
                playersTurnTextView.visibility = View.VISIBLE
                Log.d(
                    "!!!",
                    "12 dealerCount =  ${dealer.hitCounter}"
                )
                Log.d(
                    "!!!",
                    "New scores for player 1 = ${player1.scoreResult} and player 2 = ${player2.scoreResult}"
                )

            }
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
            listOf(
                cardValues[10],
                cardValues[11],
                cardValues[12],
                cardValues[13],
                cardValues[14]
            )
        Log.d(
            "!!!",
            "New scores for player 1 = ${player1.scoreResult} and player 2 = ${player2.scoreResult}"
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
        winnerLoseImgPlayer1.visibility = View.INVISIBLE
        winnerLoseImgPlayer2.visibility = View.INVISIBLE
        //All cards value is set to 0
        cardValues = MutableList(15) { 0 }
        //Counters reset

        hitCounter = 0

        dealer.hitCounter = 0
        //Score reset
        player1.scoreResult = 0
        player2.scoreResult = 0
        dealer.scoreResult = 0
        updateScore()



        playersTurnTextView.setText(R.string.player1_turn)

        Log.d("!!!", "PLay again runs")

        Log.d(
            "!!!",
            "20 dealerCount =  ${dealer.hitCounter}"
        )
        // generateCards()
    }

    /**
     * All combinations to win, lose or tie.
     */
    fun checkWin() {

        if (player1.scoreResult == 21 && cardValues[5] + cardValues[6] == 21) {

            redJill(player1)

            Log.d("!!!", "check win 1 and dealerCount = ${dealer.hitCounter}")

        }
        if (player2.scoreResult == 21 && cardValues[10] + cardValues[11] == 21) {

            redJill(player2)
            Log.d("!!!", "check win 2 and dealerCount = ${dealer.hitCounter}")

            //Charlie-rule
        }
        if (player1.hitCounter == 3 && player1.scoreResult < 21) {
            winning(player1)
            Log.d("!!!", "check win 3 and dealerCount = ${dealer.hitCounter}")

        }
        if (player2.hitCounter == 6 && player2.scoreResult < 21) {
            winning(player2)
            Log.d("!!!", "check win 4 and dealerCount = ${dealer.hitCounter}")

        }
        if (player1.scoreResult > 21) {
            loosing(player1)
            Log.d("!!!", "check win 5 and dealerCount = ${dealer.hitCounter}")
        }
        if (player2.scoreResult > 21) {
            loosing(player2)
            Log.d("!!!", "check win 6 and dealerCount = ${dealer.hitCounter}")
        }
        if (dealer.scoreResult > 21) {


            if (player1.scoreResult < 22) {
                winning(player1)
            }
            if (player2.scoreResult < 22) {
                winning(player2)
            }
            Log.d("!!!", "check win 7 and dealerCount = ${dealer.hitCounter}")
            gameOver()
            return
        }
        if (player1.scoreResult < dealer.scoreResult && dealer.scoreResult > 17 && dealer.scoreResult < 22) {
            Log.d("!!!", "check win 8 and dealerCount = ${dealer.hitCounter}")
            loosing(player1)
        }
        if (player2.scoreResult < dealer.scoreResult && dealer.scoreResult > 17 && dealer.scoreResult < 22) {
            Log.d("!!!", "check win 9 and dealerCount = ${dealer.hitCounter}")
            loosing(player2)

        }
        if (player1.scoreResult > dealer.scoreResult && dealer.scoreResult > 17 && player1.scoreResult < 22) {

            Log.d("!!!", "check win 10 and dealerCount = ${dealer.hitCounter}")
            winning(player1)

        }
        if (player2.scoreResult > dealer.scoreResult && dealer.scoreResult > 17 && player2.scoreResult < 22) {

            Log.d("!!!", "check win 20 and dealerCount = ${dealer.hitCounter}")
            winning(player2)

        }
        if (player1.scoreResult == dealer.scoreResult && dealer.scoreResult > 17) {
            Log.d("!!!", "check win 11 and dealerCount = ${dealer.hitCounter}")
            tie(player1)
        }
        if (player2.scoreResult == dealer.scoreResult && dealer.scoreResult > 17) {
            Log.d("!!!", "check win 12 and dealerCount = ${dealer.hitCounter}")
            tie(player2)

        } else {
            Log.d("!!!", "check win 13 and dealerCount = ${dealer.hitCounter}")
            return
        }
    }

    /**
     * What happens if checkWin() finds a winning game..
     */

    fun redJill(player: Player) {

        if (player == player1) {

            if (winnerLoseImgPlayer1.isVisible) {
                return
            } else {
                playersTurnTextView.text = getText(R.string.player2_turn)
                winnerLoseImgPlayer1.setImageResource(R.drawable.you_win)
                winnerLoseImgPlayer1.visibility = View.VISIBLE
                hitCounter = 3
                winCount++
            }
        } else if (player == player2) {
            if (winnerLoseImgPlayer2.isVisible) {
                return
            } else {
                winnerLoseImgPlayer2.setImageResource(R.drawable.you_win)
                winnerLoseImgPlayer2.visibility = View.VISIBLE
                winCount++
            }
        }

        gameOver()
    }

    fun winning(player: Player) {

        if (player == player1) {
            if (winnerLoseImgPlayer1.isVisible) {
                return
            }
            if (winnerLoseImgPlayer2.isVisible) {
                winnerLoseImgPlayer1.setImageResource(R.drawable.you_win)
                winnerLoseImgPlayer1.visibility = View.VISIBLE
                winCount++
                gameOver()
            } else {
                playersTurnTextView.text = getText(R.string.player2_turn)
                hitCounter = 3
                winnerLoseImgPlayer1.setImageResource(R.drawable.you_win)
                winnerLoseImgPlayer1.visibility = View.VISIBLE
                winCount++
            }
        } else if (player == player2) {

            if (winnerLoseImgPlayer2.isVisible) {
                return
            } else {

                winnerLoseImgPlayer2.setImageResource(R.drawable.you_win)
                winnerLoseImgPlayer2.visibility = View.VISIBLE
                winCount++
            }
        }
        gameOver()
    }

    fun gameOver() {

        if (winnerLoseImgPlayer1.isVisible && winnerLoseImgPlayer2.isVisible) {
            gameCount++
            GameStatsHandler.saveGameStats(this, gameCount, winCount, lostCount, tieCount)
            standButton.isEnabled = true
            hitButton.isEnabled = false
            standButton.text = getString(R.string.deal_button)
            playersTurnTextView.visibility = View.INVISIBLE
            playersTurnCardview.visibility = View.INVISIBLE
            dealer.hitCounter = 0


            Log.d(
                "!!!",
                " 30 dealerCount =  ${dealer.hitCounter}"
            )
        }

    }

    fun tie(player: Player) {

        if (player == player1) {
            if (winnerLoseImgPlayer1.isVisible) {
                return
            } else {
                winnerLoseImgPlayer1.setImageResource(R.drawable.push)
                winnerLoseImgPlayer1.visibility = View.VISIBLE
            }
        }
        if (player == player2) {
            if (winnerLoseImgPlayer2.isVisible) {
                return
            } else {

                winnerLoseImgPlayer2.setImageResource(R.drawable.push)
                winnerLoseImgPlayer2.visibility = View.VISIBLE
            }
        }

        tieCount++
        if (player1.scoreResult > 1 && winnerLoseImgPlayer2.isVisible) {
            gameOver()
        }
        gameOver()
    }

    /**
     * What happens if checkWin() finds a loosing game.
     */
    fun loosing(player: Player) {

        if (player == player1) {

            if (winnerLoseImgPlayer1.isVisible) {
                Log.d("!!!", "check win 5 done and dealerCount = ${dealer.hitCounter}")
                return
            } else {
                winnerLoseImgPlayer1.setImageResource(R.drawable.you_lose)
                winnerLoseImgPlayer1.visibility = View.VISIBLE
                playersTurnTextView.text = getText(R.string.player2_turn)
                hitCounter = 3
                lostCount++
                Log.d("!!!", "check win 5 done 2 and dealerCount = ${dealer.hitCounter}")
                gameOver()

            }

        } else if (player == player2) {

            if (winnerLoseImgPlayer2.isVisible) {
                return
            } else {

                winnerLoseImgPlayer2.setImageResource(R.drawable.you_lose)
                winnerLoseImgPlayer2.visibility = View.VISIBLE
                lostCount++
                gameOver()
            }
            if (dealer.hitCounter > 0) {
                dealerPlays()
            }
        }
    }


    fun dealerPlays() {
        dealer.hitCounter = 3

        val handler = Handler(Looper.getMainLooper())

        if (winnerLoseImgPlayer1.isVisible && winnerLoseImgPlayer2.isVisible) {
            Log.d("!!!", "RETURN RETURN")
            return

        }
        handler.postDelayed({

            standButton.isEnabled = false
            hitButton.isEnabled = false
            if (dealer.hitCounter == 3 && dealer.scoreResult < 18) {
                cards[1].setImageResource(generateCardsFromList[1].cardName)
                cardValues[1] = generateCardsFromList[1].value
                cards[1].visibility = View.VISIBLE
                updateScore()
                checkWin()
                gameOver()
                dealer.hitCounter++

                Log.d(
                    "!!!",
                    "40 dealerCount in dealerplays()=  ${dealer.hitCounter}"
                )
            }
        }, 1000)

        handler.postDelayed({
            runOnUiThread {
                if (dealer.hitCounter == 4 && dealer.scoreResult < 18) {
                    Log.d(
                        "!!!",
                        "41-1 dealerCount in dealerplays()=  ${dealer.hitCounter}"
                    )

                    cardValues[2] = generateCardsFromList[2].value
                    cards[2].visibility = View.VISIBLE
                    updateScore()
                    checkWin()
                    gameOver()
                    Log.d(
                        "!!!",
                        "41-2 dealerCount in dealerplays()=  ${dealer.hitCounter}"
                    )
                    dealer.hitCounter++

                    Log.d("!!!", "11 dealer plays ")
                    Log.d(
                        "!!!",
                        "41-3 dealerCount in dealerplays()=  ${dealer.hitCounter}"
                    )
                }
            }
        }, 3000)

        handler.postDelayed({
            runOnUiThread {

                if (dealer.hitCounter == 5 && dealer.scoreResult < 18) {
                    cardValues[3] = generateCardsFromList[3].value
                    cards[3].visibility = View.VISIBLE
                    updateScore()
                    checkWin()
                    gameOver()
                    dealer.hitCounter++
                    Log.d("!!!", "12 dealer plays ")
                    Log.d(
                        "!!!",
                        "42 dealerCount in dealerplays()=  ${dealer.hitCounter}"
                    )

                }
            }
        }, 5000)

        handler.postDelayed({
            runOnUiThread {
                if (dealer.hitCounter == 6 && dealer.scoreResult < 18) {
                    cardValues[4] = generateCardsFromList[4].value
                    cards[4].visibility = View.VISIBLE
                    updateScore()
                    checkWin()
                    gameOver()
                    Log.d("!!!", "13 dealer plays ")
                    Log.d(
                        "!!!",
                        "43 dealerCount in dealerplays()=  ${dealer.hitCounter}"
                    )
                }
                gameOver()
            }
        }, 7000)
    }
}
