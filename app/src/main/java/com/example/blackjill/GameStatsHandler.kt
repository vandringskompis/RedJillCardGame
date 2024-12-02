package com.example.blackjill

import android.content.Context

object GameStatsHandler {

    fun saveMultiGameStats(context : Context, multiGameCount: Int, multiWinCount : Int, multiLostCount : Int, multiTieCount: Int) {
        val sharedPreferences = context.getSharedPreferences("GamesStats", Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()
        editor.putInt("MULTI_GAME_COUNT", multiGameCount)
        editor.putInt("MULTI_WIN_COUNT", multiWinCount)
        editor.putInt("MULTI_LOST_COUNT", multiLostCount)
        editor.putInt("MULTI_TIE_COUNT", multiTieCount)
        editor.apply()
    }

    fun saveGameStats(context: Context, gameCount : Int, winCount : Int, lostCount : Int, tieCount : Int) {
        val sharedPreferences = context.getSharedPreferences("GamesStats", Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()
        editor.putInt("GAME_COUNT", gameCount)
        editor.putInt("WIN_COUNT", winCount)
        editor.putInt("LOST_COUNT", lostCount)
        editor.putInt("TIE_COUNT", tieCount)
        editor.apply()
    }

    fun getMultiGameStats(context: Context) : MultiGameStats{
        val sharedPreferences = context.getSharedPreferences("GamesStats", Context.MODE_PRIVATE)
        val multiGameCount = sharedPreferences.getInt("MULTI_GAME_COUNT", 0)
        val multiWinCount = sharedPreferences.getInt("MULTI_WIN_COUNT", 0)
        val multiLostCount = sharedPreferences.getInt("MULTI_LOST_COUNT", 0)
        val multiTieCount = sharedPreferences.getInt("MULTI_TIE_COUNT", 0)

        return MultiGameStats(multiGameCount, multiWinCount, multiLostCount, multiTieCount)
    }

    fun getGameStats(context: Context) : GameStats {
        val sharedPreferences = context.getSharedPreferences("GamesStats", Context.MODE_PRIVATE)
        val gameCount = sharedPreferences.getInt("GAME_COUNT", 0)
        val winCount = sharedPreferences.getInt("WIN_COUNT", 0)
        val lostCount = sharedPreferences.getInt("LOST_COUNT", 0)
        val tieCount = sharedPreferences.getInt("TIE_COUNT", 0)

        return GameStats(gameCount, winCount, lostCount, tieCount)

    }
}