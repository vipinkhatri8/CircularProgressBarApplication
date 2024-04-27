package com.example.circularprogressbarapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class MainActivity : AppCompatActivity() {
    private lateinit var customProgressBar: CircularProgressBar3
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        customProgressBar = findViewById(R.id.customProgressBar1)

        var progressStatus = 0
        val handler = Handler()

        // Start long running operation in a background thread
        Thread {
            while (progressStatus < 100) {
                progressStatus++
                // Update the progress bar and display the
                // current value in the text view
                handler.post {
                    customProgressBar.setProgress(progressStatus)
                }
                try {
                    // Sleep for 200 milliseconds.
                    Thread.sleep(25)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()

    }
}