package com.seekting.kotlindemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.seekting.kotlindemo.algorithm.Person
import com.seekting.kotlindemo.algorithm.lastChar


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("seekting", "hello")
        com.seekting.kotlindemo.algorithm.max(1, 2)

        var person = Person("seekting", 23)
        person.lastChar(1);

    }

    override fun onPause() {
        super.onPause()

    }

    override fun onStart() {
        super.onStart()
    }

}



