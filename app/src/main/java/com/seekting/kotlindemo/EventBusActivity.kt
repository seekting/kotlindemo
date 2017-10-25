package com.seekting.kotlindemo

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.seekting.kotlindemo.databinding.EventbusActivityBinding
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

const private val DEBUG = true
const private val TAG = "EventBusActivity.kt"

/**
 * Created by Administrator on 2017/10/25.
 */
class EventBusActivity : AppCompatActivity() {
    lateinit var mEventBusViewModel: EventBusViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        val binding: EventbusActivityBinding = DataBindingUtil.setContentView(this, R.layout.eventbus_activity)
        mEventBusViewModel = EventBusViewModel()
        binding.eventBusViewModel = mEventBusViewModel
//        setContentView(R.layout.eventbus_activity)
//        val button = Button(this)
//        button.text = "post"
//        button.setOnClickListener({
//            EventBus.getDefault().post(MessageEvent())
//
//
//        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(msg: MessageEvent) {
        if (DEBUG) {
            Log.d(TAG, "EventBusActivity.onMessageEvent()")
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}

class MessageEvent {

}