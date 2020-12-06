package aoc2015

import java.lang.IllegalStateException
import java.util.*
import kotlin.math.max

data class State(
    val bossHitPoints: Int,
    val playerHitPoints: Int,
    val playerMana: Int,
    val spend: Int,
    val shieldTimer: Int,
    val poisonTimer: Int,
    val rechargeTimer: Int
)

data class Result(val completeCost: Int? = null, val state: List<State> = listOf())

fun applyEffects(state: State, playerPenalty: Int): State {
    val poisonDamage = 3
    val rechargeMana = 101

    val bossHitPointReduction = if (state.poisonTimer > 0) poisonDamage else 0
    val manaIncrease = if (state.rechargeTimer > 0) rechargeMana else 0

    return State(
        state.bossHitPoints - bossHitPointReduction,
        state.playerHitPoints - playerPenalty,
        state.playerMana + manaIncrease,
        state.spend,
        if (state.shieldTimer > 0) state.shieldTimer - 1 else 0,
        if (state.poisonTimer > 0) state.poisonTimer - 1 else 0,
        if (state.rechargeTimer > 0) state.rechargeTimer - 1 else 0
    )
}

fun magicMissile(state: State, bossDamage: Int): Result {
    val missileCost = 53
    val missileDamage = 4

    if (state.playerMana < missileCost) {
        return Result()
    }

    return bossTurn(
        State(
            state.bossHitPoints - missileDamage,
            state.playerHitPoints,
            state.playerMana - missileCost,
            state.spend + missileCost,
            state.shieldTimer,
            state.poisonTimer,
            state.rechargeTimer
        ),
        bossDamage
    )
}

fun drain(state: State, bossDamage: Int): Result {
    val drainCost = 73
    val drainDamage = 2
    val drainHeal = 2

    if (state.playerMana < drainCost) {
        return Result()
    }
    return bossTurn(
        State(
            state.bossHitPoints - drainDamage,
            state.playerHitPoints + drainHeal,
            state.playerMana - drainCost,
            state.spend + drainCost,
            state.shieldTimer,
            state.poisonTimer,
            state.rechargeTimer
        ),
        bossDamage
    )
}

fun shield(state: State, bossDamage: Int): Result {
    val shieldCost = 113
    val shieldTimer = 6

    if (state.playerMana < shieldCost || state.shieldTimer > 0) {
        return Result()
    }
    return bossTurn(
        State(
            state.bossHitPoints,
            state.playerHitPoints,
            state.playerMana - shieldCost,
            state.spend + shieldCost,
            shieldTimer,
            state.poisonTimer,
            state.rechargeTimer
        ),
        bossDamage
    )
}

fun poison(state: State, bossDamage: Int): Result {
    val poisonCost = 173
    val poisonTimer = 6

    if (state.playerMana < poisonCost || state.poisonTimer > 0) {
        return Result()
    }
    return bossTurn(
        State(
            state.bossHitPoints,
            state.playerHitPoints,
            state.playerMana - poisonCost,
            state.spend + poisonCost,
            state.shieldTimer,
            poisonTimer,
            state.rechargeTimer
        ),
        bossDamage
    )
}

fun recharge(state: State, bossDamage: Int): Result {
    val rechargeCost = 229
    val rechargeTimer = 5

    if (state.playerMana < rechargeCost || state.rechargeTimer > 0) {
        return Result()
    }
    return bossTurn(
        State(
            state.bossHitPoints,
            state.playerHitPoints,
            state.playerMana - rechargeCost,
            state.spend + rechargeCost,
            state.shieldTimer,
            state.poisonTimer,
            rechargeTimer
        ),
        bossDamage
    )
}

fun bossTurn(state: State, bossDamage: Int): Result {

    val shieldEffect = 7

    val s2 = applyEffects(state, 0)
    if (s2.bossHitPoints <= 0) {
        return Result(completeCost = s2.spend)
    }
    val shield = if (s2.shieldTimer > 0) shieldEffect else 0
    val damage = max(1, bossDamage - shield)
    val playerHitPoints = s2.playerHitPoints - damage
    if (playerHitPoints <= 0) {
        return Result()
    }
    return Result(
        state = listOf(
            State(
                s2.bossHitPoints,
                playerHitPoints,
                s2.playerMana,
                s2.spend,
                s2.shieldTimer,
                s2.poisonTimer,
                s2.rechargeTimer
            )
        )
    )
}

fun playerTurn(state: State, bossDamage: Int, playerPenalty: Int): Result {

    val s2 = applyEffects(state, playerPenalty)

    if (s2.playerHitPoints <= 0) {
        return Result()
    }

    if (s2.bossHitPoints <= 0) {
        return Result(completeCost = s2.spend)
    }

    val results = listOf(
        magicMissile(s2, bossDamage),
        drain(s2, bossDamage),
        shield(s2, bossDamage),
        poison(s2, bossDamage),
        recharge(s2, bossDamage)
    )

    val costs = results.filter { it.completeCost != null }.map { it.completeCost!! }
    return if (costs.isNotEmpty()) {
        Result(completeCost = costs.minOrNull())
    } else {
        Result(state = results.flatMap { it.state })
    }
}
private fun run(
    bossHitPoints: Int,
    playerHitPoints: Int,
    playerMana: Int,
    bossDamage: Int,
    playerPenaltyPart1: Int
): Int {
    val nextStates = PriorityQueue<State> { a, b -> a.spend - b.spend }
    nextStates.add(
        State(
            bossHitPoints,
            playerHitPoints,
            playerMana,
            0,
            0,
            0,
            0
        )
    )

    while (nextStates.isNotEmpty()) {
        val next = nextStates.poll()
        val result = playerTurn(
            next,
            bossDamage,
            playerPenaltyPart1
        )

        val maybeCost = listOfNotNull(result.completeCost)
        if (maybeCost.isNotEmpty()) {
            return maybeCost[0]
        } else {
            nextStates.addAll(result.state)
        }
    }
    throw IllegalStateException("Result not found")
}

fun main() {
    val bossHitPoints = 58
    val bossDamage = 9
    val playerHitPoints = 50
    val playerMana = 500
    val playerPenaltyPart1 = 0
    val playerPenaltyPart2 = 1

    println("Part 1: ${run(bossHitPoints, playerHitPoints, playerMana, bossDamage, playerPenaltyPart1)}")
    println("Part 2: ${run(bossHitPoints, playerHitPoints, playerMana, bossDamage, playerPenaltyPart2)}")
}

