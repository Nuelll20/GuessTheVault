package com.example.guessthevault.model

import kotlin.math.abs
class GuessGame {

    private var secretNumber: Int = generateSecretNumber()

    var score: Int = 100
        private set

    var attempts: Int = 0
        private set

    private fun generateSecretNumber(): Int {
        return (0..100).random()
    }

    fun checkGuess(guess: Int): String {
        attempts += 1

        if (guess == secretNumber) {
            return "CORRECT"
        }

        score -= 5

        if (score <= 0) {
            score = 0
            return "GAME_OVER"
        }

        val difference = abs(guess - secretNumber)

        return when {
            difference <= 5 && guess < secretNumber -> {
                "CLOSE_LOW"
            }

            difference <= 5 && guess > secretNumber -> {
                "CLOSE_HIGH"
            }

            guess < secretNumber -> {
                "TOO_LOW"
            }

            else -> {
                "TOO_HIGH"
            }
        }
    }

    fun resetGame() {
        secretNumber = generateSecretNumber()
        score = 100
        attempts = 0
    }

    fun restoreState(
        savedSecretNumber: Int,
        savedScore: Int,
        savedAttempts: Int
    ) {
        secretNumber = savedSecretNumber
        score = savedScore
        attempts = savedAttempts
    }

    fun getSecretNumber(): Int {
        return secretNumber
    }

    fun isGameOver(): Boolean {
        return score <= 0
    }
}