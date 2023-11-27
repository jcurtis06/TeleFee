package io.jcurtis.telefee.utils

import org.bukkit.entity.Player
import kotlin.math.floor
import kotlin.math.pow


object ExpUtils {
    // Calculate amount of EXP needed to level up
    fun getExpToLevelUp(level: Int): Int {
        return when {
            level <= 15 -> 2 * level + 7
            level <= 30 -> 5 * level - 38
            else -> 9 * level - 158
        }
    }

    // Calculate total experience up to a level
    fun getExpAtLevel(level: Int): Int {
        return when {
            level <= 16 -> (level.toDouble().pow(2.0) + 6 * level).toInt()
            level <= 31 -> (2.5 * level.toDouble().pow(2.0) - 40.5 * level + 360.0).toInt()
            else -> (4.5 * level.toDouble().pow(2.0) - 162.5 * level + 2220.0).toInt()
        }
    }

    // Calculate player's current EXP amount
    fun getPlayerExp(player: Player): Int {
        var exp = 0
        val level = player.level

        // Get the amount of XP in past levels
        exp += getExpAtLevel(level)

        // Get amount of XP towards next level
        exp += Math.round(getExpToLevelUp(level) * player.exp).toInt()

        return exp
    }

    // Give or take EXP
    fun changePlayerExp(player: Player, exp: Int): Int {
        // Get player's current exp
        val currentExp = getPlayerExp(player)

        // Reset player's current exp to 0
        player.setExp(0F)
        player.setLevel(0)

        // Give the player their exp back, with the difference
        val newExp = currentExp + exp
        player.giveExp(newExp)

        // Return the player's new exp amount
        return newExp
    }

    fun levelToPoints(currentLevel: Int, level: Double): Int {
        return if (level % 1 == 0.0) {
            getExpAtLevel(level.toInt())
        } else {
            floor(level * getExpAtLevel(currentLevel)).toInt()
        }
    }
}