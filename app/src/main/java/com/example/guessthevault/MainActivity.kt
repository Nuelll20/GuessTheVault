package com.example.guessthevault

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.guessthevault.model.GuessGame

class MainActivity : AppCompatActivity() {

    private lateinit var tvScore: TextView
    private lateinit var tvAttempts: TextView
    private lateinit var tvStatus: TextView
    private lateinit var etGuess: EditText
    private lateinit var btnGuess: Button
    private lateinit var btnReset: Button

    private val game = GuessGame()
    private var gameFinished = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        connectViews()

        if (savedInstanceState == null) {
            updateGameInformation()
        } else {
            restoreGameState(savedInstanceState)
        }

        btnGuess.setOnClickListener {
            submitGuess()
        }

        btnReset.setOnClickListener {
            resetGame()
        }
    }

    private fun connectViews() {
        tvScore = findViewById(R.id.tvScore)
        tvAttempts = findViewById(R.id.tvAttempts)
        tvStatus = findViewById(R.id.tvStatus)
        etGuess = findViewById(R.id.etGuess)
        btnGuess = findViewById(R.id.btnGuess)
        btnReset = findViewById(R.id.btnReset)
    }

    private fun submitGuess() {
        val input = etGuess.text.toString().trim()
        val guessedNumber = input.toIntOrNull()

        if (guessedNumber == null) {
            etGuess.error = "Masukkan angka terlebih dahulu"
            return
        }

        if (guessedNumber !in 0..100) {
            etGuess.error = "Angka harus berada di antara 0 sampai 100"
            return
        }

        etGuess.error = null

        when (game.checkGuess(guessedNumber)) {
            "CORRECT" -> {
                tvStatus.text = "Vault terbuka! Angkanya adalah $guessedNumber."
                tvStatus.setTextColor(Color.parseColor("#3FB950"))
                finishGame()
            }

            "TOO_LOW" -> {
                tvStatus.text = "Terlalu rendah! Coba angka yang lebih besar."
                tvStatus.setTextColor(Color.parseColor("#F5C451"))
            }

            "TOO_HIGH" -> {
                tvStatus.text = "Terlalu tinggi! Coba angka yang lebih kecil."
                tvStatus.setTextColor(Color.parseColor("#F5C451"))
            }

            "GAME_OVER" -> {
                tvStatus.text =
                    "Game over! Angka rahasianya ${game.getSecretNumber()}."
                tvStatus.setTextColor(Color.parseColor("#F85149"))
                finishGame()
            }
        }

        updateGameInformation()
        etGuess.text.clear()
    }

    private fun updateGameInformation() {
        tvScore.text = "SKOR\n${game.score}"
        tvAttempts.text = "PERCOBAAN\n${game.attempts}"
    }

    private fun finishGame() {
        gameFinished = true
        updateGameControls()
    }

    private fun updateGameControls() {
        etGuess.isEnabled = !gameFinished
        btnGuess.isEnabled = !gameFinished

        btnReset.visibility = if (gameFinished) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun resetGame() {
        game.resetGame()
        gameFinished = false

        updateGameInformation()
        updateGameControls()

        tvStatus.text = "Masukkan tebakan pertama Anda"
        tvStatus.setTextColor(Color.WHITE)

        etGuess.text.clear()
        etGuess.requestFocus()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(KEY_SECRET_NUMBER, game.getSecretNumber())
        outState.putInt(KEY_SCORE, game.score)
        outState.putInt(KEY_ATTEMPTS, game.attempts)
        outState.putString(KEY_STATUS_TEXT, tvStatus.text.toString())
        outState.putInt(KEY_STATUS_COLOR, tvStatus.currentTextColor)
        outState.putString(KEY_INPUT, etGuess.text.toString())
        outState.putBoolean(KEY_GAME_FINISHED, gameFinished)

        super.onSaveInstanceState(outState)
    }

    private fun restoreGameState(savedState: Bundle) {
        game.restoreState(
            savedSecretNumber = savedState.getInt(
                KEY_SECRET_NUMBER,
                game.getSecretNumber()
            ),
            savedScore = savedState.getInt(KEY_SCORE, 100),
            savedAttempts = savedState.getInt(KEY_ATTEMPTS, 0)
        )

        gameFinished = savedState.getBoolean(KEY_GAME_FINISHED, false)

        tvStatus.text = savedState.getString(KEY_STATUS_TEXT)
            ?: "Masukkan tebakan pertama Anda"

        tvStatus.setTextColor(
            savedState.getInt(KEY_STATUS_COLOR, Color.WHITE)
        )

        etGuess.setText(savedState.getString(KEY_INPUT, ""))

        updateGameInformation()
        updateGameControls()
    }

    companion object {
        private const val KEY_SECRET_NUMBER = "secret_number"
        private const val KEY_SCORE = "score"
        private const val KEY_ATTEMPTS = "attempts"
        private const val KEY_STATUS_TEXT = "status_text"
        private const val KEY_STATUS_COLOR = "status_color"
        private const val KEY_INPUT = "input"
        private const val KEY_GAME_FINISHED = "game_finished"
    }
}