package com.example.guessthevault.model

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

        return if (guess < secretNumber) {
            "TOO_LOW"
        } else {
            "TOO_HIGH"
        }
    }

    fun resetGame() {
        secretNumber = generateSecretNumber()
        score = 100
        attempts = 0
    }

    fun getSecretNumber(): Int {
        return secretNumber
    }

    fun isGameOver(): Boolean {
        return score <= 0
    }
}