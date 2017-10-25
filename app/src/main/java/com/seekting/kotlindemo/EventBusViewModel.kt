package com.seekting.kotlindemo

import android.databinding.BaseObservable
import android.util.Log
import android.view.View

/**
 * Created by Administrator on 2017/10/25.
 */
const private val DEBUG = true
const private val TAG = "EventBusViewModel.kt"

class EventBusViewModel : BaseObservable() {
    fun onClickSend(v: View) {
        if (DEBUG) {
            Log.d(TAG, "EventBusViewModel.onClickSend()")
        }


    }

    fun onClickRegister(v: View) {
         if (DEBUG) {
           Log.d(TAG, "EventBusViewModel.onClickRegister()")
          }
         
    }
    fun onClickUnregister(v: View) {
         if (DEBUG) {
           Log.d(TAG, "EventBusViewModel.onClickUnregister()")
          }
         
    }

}