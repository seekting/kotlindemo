package com.seekting.kotlindemo

import android.databinding.BaseObservable
import android.util.Log
import android.view.View
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by Administrator on 2017/10/25.
 */
const private val DEBUG = true
const private val TAG = "EventBusViewModel.kt"

class EventBusViewModel : BaseObservable() {
    var value: Int = 0

    init {
        val builder = EventBus.builder()
        builder.eventInheritance(true)
        builder.installDefaultEventBus()

    }

    fun onClickSend(v: View) {
        if (DEBUG) {
            Log.d(TAG, "EventBusViewModel.onClickSend()")
        }


        EventBus.getDefault().post(MessageEvent())
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true, priority = 1)
    fun onMessageEvent(msg: MessageEvent) {
        if (DEBUG) {
            Log.d(TAG, "EventBusViewModel.onMessageEvent()")
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onStickyEvent(msg: StickyEvent) {
        if (DEBUG) {
            Log.d(TAG, "EventBusViewModel.onStickyEvent()${msg.value}")
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onSubStickyEvent(msg: SubStickyEvent) {
        if (DEBUG) {
            Log.d(TAG, "EventBusViewModel.onSubStickyEvent()${msg.value}")
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onXSubStickyEvent(msg: XSubStickyEvent) {
        if (DEBUG) {
            Log.d(TAG, "EventBusViewModel.onXSubStickyEvent()")
        }


    }

    fun onClickSendSubSticky(view: View) {
        if (DEBUG) {
            Log.d(TAG, "EventBusViewModel.onClickSendSubSticky()")
        }

        EventBus.getDefault().postSticky(SubStickyEvent(value++))
    }

    fun getSticky(v: View) {
        val stickyEvent = EventBus.getDefault().getStickyEvent(StickyEvent::class.java)
        if (DEBUG) {
            Log.d(TAG, "EventBusViewModel.getSticky()stickyEvent=" + stickyEvent?.value)
        }
    }

    fun removeSticky(v: View) {
        val stickyEvent = EventBus.getDefault().removeStickyEvent(StickyEvent::class.java)
        if (DEBUG) {
            Log.d(TAG, "EventBusViewModel.removeSticky()stickyEvent=" + stickyEvent?.value)
        }
    }

    fun onClickRegister(v: View) {
        if (DEBUG) {
            val isAssignableFrom = SubStickyEvent::class.java.isAssignableFrom(StickyEvent::class.java)
            val isAssignableFrom2 = StickyEvent::class.java.isAssignableFrom(SubStickyEvent::class.java)
            Log.d(TAG, "isAssignableFrom=$isAssignableFrom,$isAssignableFrom2")
            Log.d(TAG, "EventBusViewModel.onClickRegister()")
        }
        EventBus.getDefault().register(this)


    }

    fun onClickUnregister(v: View) {
        if (DEBUG) {
            Log.d(TAG, "EventBusViewModel.onClickUnregister()")
        }
        EventBus.getDefault().unregister(this)

    }

    fun onClickSendSticky(v: View) {
        if (DEBUG) {
            Log.d(TAG, "EventBusViewModel.onClickSendSticky()")
        }

        EventBus.getDefault().postSticky(StickyEvent(value++))

    }

}

class MessageEvent {

}

open class StickyEvent(val value: Int) {

}

class SubStickyEvent(value: Int) : StickyEvent(value) {


}

class XSubStickyEvent(value: Int) {


}

