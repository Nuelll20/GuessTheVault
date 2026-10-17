package com.example.guessthevault.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GuessGameTest {

    private lateinit var game: GuessGame

    @Before
    fun setUp() {
        game = GuessGame()

        game.restoreState(
            savedSecretNumber = 80,
            savedScore = 100,
            savedAttempts = 0
        )
    }

    @Test
    fun `correct guess returns CORRECT`() {
        val result = game.checkGuess(80)

        assertEquals("CORRECT", result)
        assertEquals(100, game.score)
        assertEquals(1, game.attempts)
    }

    @Test
    fun `close lower guess returns CLOSE_LOW`() {
        val result = game.checkGuess(77)

        assertEquals("CLOSE_LOW", result)
        assertEquals(95, game.score)
        assertEquals(1, game.attempts)
    }

    @Test
    fun `close higher guess returns CLOSE_HIGH`() {
        val result = game.checkGuess(81)

        assertEquals("CLOSE_HIGH", result)
        assertEquals(95, game.score)
        assertEquals(1, game.attempts)
    }

    @Test
    fun `far lower guess returns TOO_LOW`() {
        val result = game.checkGuess(50)

        assertEquals("TOO_LOW", result)
        assertEquals(95, game.score)
    }

    @Test
    fun `far higher guess returns TOO_HIGH`() {
        val result = game.checkGuess(100)

        assertEquals("TOO_HIGH", result)
        assertEquals(95, game.score)
    }

    @Test
    fun `score reaches zero returns GAME_OVER`() {
        game.restoreState(
            savedSecretNumber = 80,
            savedScore = 5,
            savedAttempts = 19
        )

        val result = game.checkGuess(20)

        assertEquals("GAME_OVER", result)
        assertEquals(0, game.score)
        assertEquals(20, game.attempts)
        assertTrue(game.isGameOver())
    }

    @Test
    fun `reset restores initial game values`() {
        game.checkGuess(20)
        game.resetGame()

        assertEquals(100, game.score)
        assertEquals(0, game.attempts)
        assertFalse(game.isGameOver())
        assertTrue(game.getSecretNumber() in 0..100)
    }
}