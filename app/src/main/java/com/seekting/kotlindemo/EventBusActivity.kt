package com.seekting.kotlindemo

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.seekting.kotlindemo.databinding.EventbusActivityBinding

const private val DEBUG = true
const private val TAG = "EventBusActivity.kt"

/**
 * Created by Administrator on 2017/10/25.
 */
class EventBusActivity : AppCompatActivity() {
    lateinit var mEventBusViewModel: EventBusViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: EventbusActivityBinding = DataBindingUtil.setContentView(this, R.layout.eventbus_activity)
        mEventBusViewModel = EventBusViewModel()
        binding.eventBusViewModel = mEventBusViewModel

    }



    override fun onDestroy() {
        super.onDestroy()
    }
}

