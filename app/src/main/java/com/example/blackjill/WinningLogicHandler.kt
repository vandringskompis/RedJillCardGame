package com.example.blackjill

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible

class WinningLogicHandler(
    var player1: Player,
    var player2: Player,
    var cardValues: MutableList<Int>,
    var dealer: Player,
    val winnerLoseImgPlayer1: ImageView,
    val winnerLoseImgPlayer2: ImageView,
    var playersTurnTextView : TextView,
    val playersTurnCardview : CardView,
    var multiWinCount : Int,
    var multiGameCount : Int,
    var multiLostCount : Int,
    var multiTieCount : Int,
    val standButton : Button,
    val hitButton : Button,
    val activity: MultiplayerGameboardActivity
) {
    /**
     * All combinations to win, lose or tie.
     */
    fun checkWin() {

        if (player1.scoreResult == 21 && cardValues[5] + cardValues[6] == 21) {
            redJill(player1)
        }
        if (player2.scoreResult == 21 && cardValues[10] + cardValues[11] == 21 || player2.hitCounter == 1 && player2.scoreResult == 21) {
            redJill(player2)
            //Charlie-rule
        }
        if (player1.hitCounter == 3 && player1.scoreResult < 21) {
            winning(player1)
        }
        if (player2.hitCounter == 6 && player2.scoreResult < 21) {
            winning(player2)
        }
        if (player1.scoreResult > 21) {
            losing(player1)
        }
        if (player2.scoreResult > 21) {
            losing(player2)
        }
        if (dealer.scoreResult > 21) {

            if (player1.scoreResult < 22) {
                winning(player1)
            }
            if (player2.scoreResult < 22) {
                winning(player2)
            }
            gameOver()
            return
        }
        if (player1.scoreResult < dealer.scoreResult && dealer.scoreResult > 16 && dealer.scoreResult < 22) {
            losing(player1)
        }
        if (player2.scoreResult < dealer.scoreResult && dealer.scoreResult > 16 && dealer.scoreResult < 22) {
            losing(player2)

        }
        if (player1.scoreResult > dealer.scoreResult && dealer.scoreResult > 16 && player1.scoreResult < 22) {
            winning(player1)

        }
        if (player2.scoreResult > dealer.scoreResult && dealer.scoreResult > 16 && player2.scoreResult < 22) {
            winning(player2)

        }
        if (dealer.hitCounter == 6 && dealer.scoreResult < 22) {
            winning(player1)
            winning(player2)
        }
        if (player1.scoreResult == dealer.scoreResult && dealer.scoreResult > 16) {
            tie(player1)
        }
        if (player2.scoreResult == dealer.scoreResult && dealer.scoreResult > 16) {
            tie(player2)

        } else {
            return
        }
    }

    /**
     * What happens if checkWin() finds a winning game..
     */

    private fun redJill(player: Player) {

        if (player == player1) {

            if (winnerLoseImgPlayer1.isVisible) {
                return
            } else {
                playersTurnTextView.text = activity.getText(R.string.player2_turn)
                winnerLoseImgPlayer1.setImageResource(R.drawable.you_win)
                winnerLoseImgPlayer1.visibility = View.VISIBLE
                player2.hitCounter = 3
               // hitCounter = 3
                multiWinCount++
            }
        } else if (player == player2) {
            if (winnerLoseImgPlayer2.isVisible) {
                return
            } else {
                winnerLoseImgPlayer2.setImageResource(R.drawable.you_win)
                winnerLoseImgPlayer2.visibility = View.VISIBLE
                multiWinCount++
            }
        }
        gameOver()
    }

    private fun winning(player: Player) {

        if (player == player1) {
            if (winnerLoseImgPlayer1.isVisible) {
                return
            }
            if (winnerLoseImgPlayer2.isVisible) {
                winnerLoseImgPlayer1.setImageResource(R.drawable.you_win)
                winnerLoseImgPlayer1.visibility = View.VISIBLE
                multiWinCount++
                gameOver()
            } else {
                playersTurnTextView.text = activity.getText(R.string.player2_turn)
                player2.hitCounter = 3
                //hitCounter = 3
                winnerLoseImgPlayer1.setImageResource(R.drawable.you_win)
                winnerLoseImgPlayer1.visibility = View.VISIBLE
                multiWinCount++
            }
        } else if (player == player2) {

            if (winnerLoseImgPlayer2.isVisible) {
                return
            } else {
                winnerLoseImgPlayer2.setImageResource(R.drawable.you_win)
                winnerLoseImgPlayer2.visibility = View.VISIBLE
                multiWinCount++
            }
        }
        gameOver()
    }

    fun updateGameCount() {
        multiGameCount++
    }

    fun gameOver() {

        if (winnerLoseImgPlayer1.isVisible && winnerLoseImgPlayer2.isVisible) {

            GameStatsHandler.saveMultiGameStats(
                activity,
                multiGameCount,
                multiWinCount,
                multiLostCount,
                multiTieCount
            )
            standButton.isEnabled = true
            hitButton.isEnabled = false
            standButton.text = activity.getString(R.string.deal_button)
            playersTurnTextView.visibility = View.INVISIBLE
            playersTurnCardview.visibility = View.INVISIBLE
            dealer.hitCounter = 0
        }
    }

    private fun tie(player: Player) {

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
        multiTieCount++
        if (player1.scoreResult > 1 && winnerLoseImgPlayer2.isVisible) {
            gameOver()
        }
        gameOver()
    }

    /**
     * What happens if checkWin() finds a loosing game.
     */
    private fun losing(player: Player) {

        if (player == player1) {

            if (winnerLoseImgPlayer1.isVisible) {
                return
            } else {
                winnerLoseImgPlayer1.setImageResource(R.drawable.you_lose)
                winnerLoseImgPlayer1.visibility = View.VISIBLE
                playersTurnTextView.text = activity.getText(R.string.player2_turn)
                player2.hitCounter = 3
                //hitCounter = 3
                multiLostCount++
                gameOver()
            }
        } else if (player == player2) {
            if (winnerLoseImgPlayer2.isVisible) {
                return
            } else {
                winnerLoseImgPlayer2.setImageResource(R.drawable.you_lose)
                winnerLoseImgPlayer2.visibility = View.VISIBLE
                multiLostCount++
                gameOver()
            }
            if (dealer.hitCounter > 0) {
                activity.dealerPlays()
            }
        }
    }
}