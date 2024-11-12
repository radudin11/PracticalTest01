package com.example.practicaltest01

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Colocviu1_MainActivity : AppCompatActivity() {
    private lateinit var goToSecondActivityBtn : Button
    private lateinit var pressMeBtn : Button
    private lateinit var pressMeTooBtn : Button
    private lateinit var editTextStanga : EditText
    private lateinit var editTextDreapta : EditText

    var leftText = 0
    var rightText = 0

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val totalPresses = result.data?.getIntExtra("totalPresses", 0) ?: 0
            Toast.makeText(this, "Register: Total presses: $totalPresses", Toast.LENGTH_SHORT).show()
        } else if (result.resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this, "Result Canceled from Second Activity", Toast.LENGTH_SHORT).show()
        }
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val currentTime = intent?.getStringExtra("currentTime")
            val arithmeticMean = intent?.getDoubleExtra("arithmeticMean", 0.0)
            val geometricMean = intent?.getDoubleExtra("geometricMean", 0.0)
            Toast.makeText(context, "Time: $currentTime\nArithmetic Mean: $arithmeticMean\nGeometric Mean: $geometricMean", Toast.LENGTH_LONG).show()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_colocviul_1_main)

        goToSecondActivityBtn = findViewById(R.id.navSecActBtn)
        pressMeBtn = findViewById(R.id.press_me)
        pressMeTooBtn = findViewById(R.id.press_me_too)
        editTextStanga = findViewById(R.id.editStanga)
        editTextDreapta = findViewById(R.id.editTextDreapta)

        pressMeBtn.setOnClickListener {
            leftText++
            editTextStanga.setText(leftText.toString())
            checkAndStartService()
        }

        pressMeTooBtn.setOnClickListener {
            rightText++
            editTextDreapta.setText(rightText.toString())
            checkAndStartService()
        }

        goToSecondActivityBtn.setOnClickListener {
            val intent = Intent(this, Colocviul_1SecondaryActivity::class.java)
            intent.putExtra("leftText", leftText)
            intent.putExtra("rightText", rightText)
            // reset values
            leftText = 0
            rightText = 0


            startForResult.launch(intent)
        }

        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        leftText = sharedPreferences.getInt("leftText", 0)
        rightText = sharedPreferences.getInt("rightText", 0)
        editTextStanga.setText(leftText.toString())
        editTextDreapta.setText(rightText.toString())


        val filter = IntentFilter().apply {
            addAction("ACTION_ONE")
        }

        registerReceiver(broadcastReceiver, filter, RECEIVER_EXPORTED)

    }

    override fun onPause() {
        super.onPause()
        // Save values
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putInt("leftText", leftText)
            putInt("rightText", rightText)
            apply()
        }
    }

    override fun onResume() {
        super.onResume()
        // Reset values in text fields
        editTextStanga.setText(leftText.toString())
        editTextDreapta.setText(rightText.toString())

    }

    private fun checkAndStartService() {
        // log trying to start service and sum
        android.util.Log.d("PracticalTest01Service", "Trying to start service")
        android.util.Log.d("PracticalTest01Service", "Sum: ${leftText + rightText}")

        if (leftText + rightText > 4) {
            val startServiceIntent = Intent(this, Colocviul_1Service::class.java)
            startServiceIntent.putExtra("leftText", leftText)
            startServiceIntent.putExtra("rightText", rightText)
            startService(startServiceIntent)
            // print toast that announces service is started
            Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show()
        }
    }
}