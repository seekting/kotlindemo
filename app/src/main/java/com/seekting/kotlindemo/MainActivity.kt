package com.seekting.kotlindemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("seekting","hello")
    }

    override fun onPause() {
        super.onPause()

    }

    override fun onStart() {
        super.onStart()
    }
}
