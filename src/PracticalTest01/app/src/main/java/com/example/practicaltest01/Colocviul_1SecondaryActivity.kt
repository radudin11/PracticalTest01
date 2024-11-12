package com.example.practicaltest01

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Colocviul_1SecondaryActivity : AppCompatActivity() {
    lateinit var cancelBtn: Button
    lateinit var okBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_colocviul_1_secondary)

        okBtn = findViewById(R.id.ok)
        cancelBtn = findViewById(R.id.cancel)

        val leftText = intent.getIntExtra("leftText", 0)
        val rightText = intent.getIntExtra("rightText", 0)
        val totalPresses = leftText + rightText

        val textView = findViewById<TextView>(R.id.textView)
        textView.text = "Total presses: $totalPresses"

        okBtn.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("totalPresses", totalPresses)
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        cancelBtn.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

}