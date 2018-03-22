package com.seekting.kotlindemo

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

/**
 * Created by Administrator on 2017/10/22.
 */
class FragmentsActivity : FragmentActivity() {

    lateinit var view: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_activitys)
        val c = supportFragmentManager.beginTransaction()
        view = findViewById(R.id.fragment1)
        val fragment = MyFragment()
        val bundle = Bundle()
        bundle.putString("title", "seekting")
        fragment.arguments = bundle
        c.replace(R.id.fragment1, fragment)
        c.commit()

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        Log.d("seekting", ".onWindowFocusChanged()hasFocus=$hasFocus width=${view.width}")
    }

}

class MyFragment1 : Fragment() {
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.d("seekting","2.onHiddenChanged()${hidden}")
    }
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        Log.d("seekting", "2.setUserVisibleHint()$isVisibleToUser")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val tv = TextView(activity)
        tv.text = "2"
        tv.setOnClickListener({
            fragmentManager.popBackStack("1", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        })
        return tv
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("seekting", "2.onDestroyView()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("seekting", "2.onDestroy()")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("seekting", "2.onDetach()")
    }

}

class MyFragment : Fragment {
    private lateinit var mContext: Activity
    private lateinit var mTitle: String
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.d("seekting","1.onHiddenChanged()${hidden}")
    }
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        Log.d("seekting", "1.setUserVisibleHint()$isVisibleToUser")
    }
    constructor() {
        Log.d("seekting", "constructor.()")
    }


    override fun onStart() {
        super.onStart()
        Log.d("seekting", "onStart.()")
    }

    override fun onResume() {
        super.onResume()
        Log.d("seekting", "onResume.()")
    }

    override fun onPause() {
        super.onPause()
        Log.d("seekting", ".onPause()")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Log.d("seekting", "onSaveInstanceState.()")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d("seekting", ".onViewStateRestored()")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context as Activity
        mTitle = arguments.getString("title")
        Log.d("seekting", ".onAttach()")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("seekting", ".onActivityCreated")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val textView = Button(mContext)
        textView.text = mTitle
        textView.setOnClickListener({
            val t = fragmentManager.beginTransaction()
            t.replace(R.id.fragment1, MyFragment1())
            t.addToBackStack("1")
            t.commit()

        })
        Log.d("seekting", ".onCreateView()")
        return textView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("seekting", "1.onDestroyView()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("seekting", "1.onDestroy()")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("seekting", "1.onDetach()")
    }
}