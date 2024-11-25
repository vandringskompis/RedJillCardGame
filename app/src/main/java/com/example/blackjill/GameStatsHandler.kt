package com.example.blackjill

import android.content.Context

object GameStatsHandler {

    fun saveGameStats(context : Context, gameCount: Int, winCount : Int, lostCount : Int, tieCount: Int) {
        val sharedPreferences = context.getSharedPreferences("GamesStats", Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()
        editor.putInt("GAME_COUNT", gameCount)
        editor.putInt("WIN_COUNT", winCount)
        editor.putInt("LOST_COUNT", lostCount)
        editor.putInt("TIE_COUNT", tieCount)
        editor.apply()
    }
    fun getGameStats(context: Context) : GameStats{
        val sharedPreferences = context.getSharedPreferences("GamesStats", Context.MODE_PRIVATE)
        val gameCount = sharedPreferences.getInt("GAME_COUNT", 0)
        val winCount = sharedPreferences.getInt("WIN_COUNT", 0)
        val lostCount = sharedPreferences.getInt("LOST_COUNT", 0)
        val tieCount = sharedPreferences.getInt("TIE_COUNT", 0)

        return GameStats(gameCount, winCount, lostCount, tieCount)
    }
}