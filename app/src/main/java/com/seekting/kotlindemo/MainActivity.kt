package com.seekting.kotlindemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var str = "abcd1234568bcd2345678091jfdaa;a;542378095237847290jfjdal"

        var length = str.length
        var currentBegin = -1
        var currentEnd = -1
        var currentSize = 0


        var maxBegin = 0
        var maxEnd = 0
        var maxSize = 0

        for (i in 0 until length) {
            var c = str[i]
            if (c in '0'..'9') {
                if (currentBegin == -1) {
                    currentBegin = i
                } else {
                    currentEnd = i

                }
                if (i == length - 1) {
                    if (currentBegin > 0) {
                        currentSize = currentEnd - currentBegin + 1
                        if (maxSize == 0 || maxSize < currentSize) {
                            maxSize = currentSize
                            maxBegin = currentBegin
                            maxEnd = currentEnd
                        }
                        currentBegin = -1
                        currentEnd = -1
                    }
                }

            } else {
                if (currentBegin > 0) {
                    currentSize = currentEnd - currentBegin + 1
                    if (maxSize == 0 || maxSize < currentSize) {
                        maxSize = currentSize
                        maxBegin = currentBegin
                        maxEnd = currentEnd
                    }
                    currentBegin = -1
                    currentEnd = -1
                }
            }
        }
        var sub = str.substring(maxBegin, maxEnd + 1)
        Log.d("seekting", "sub=" + sub)

    }

    override fun onPause() {
        super.onPause()

    }

    override fun onStart() {
        super.onStart()
    }
}
