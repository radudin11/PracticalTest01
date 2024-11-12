package com.example.practicaltest01

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import java.text.DateFormat
import java.util.Calendar

class Colocviul_1Service : Service() {

    private lateinit var handlerThread: HandlerThread
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val leftText = intent?.getIntExtra("leftText", 0) ?: 0
        val rightText = intent?.getIntExtra("rightText", 0) ?: 0

        handlerThread = HandlerThread("PracticalTest01ServiceThread")
        handlerThread.start()
        handler = Handler(handlerThread.looper)

        runnable = Runnable {
            val currentTime = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(Calendar.getInstance().time)
            val sum = leftText + rightText

            val broadcastIntent = Intent()
            broadcastIntent.action = "ACTION_ONE"
            broadcastIntent.putExtra("currentTime", currentTime)
            broadcastIntent.putExtra("sum", sum)


            // log the broadcast
            android.util.Log.d("PracticalTest01Service", "Broadcasting: $broadcastIntent")

            sendBroadcast(broadcastIntent)
        }

        handler.postDelayed(runnable, 5000) // Run after 5 seconds delay
        return START_STICKY
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        handlerThread.quitSafely()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}