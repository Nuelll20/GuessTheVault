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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        connectViews()
        updateGameInformation()

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
        etGuess.isEnabled = false
        btnGuess.isEnabled = false
        btnReset.visibility = View.VISIBLE
    }

    private fun resetGame() {
        game.resetGame()

        updateGameInformation()

        tvStatus.text = "Masukkan tebakan pertama Anda"
        tvStatus.setTextColor(Color.WHITE)

        etGuess.text.clear()
        etGuess.isEnabled = true
        btnGuess.isEnabled = true
        btnReset.visibility = View.GONE

        etGuess.requestFocus()
    }
}