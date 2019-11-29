package com.ccooy.apidemos.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ccooy.apidemos.R

class FragmentRetainInstance : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(
                android.R.id.content,
                UiFragment()
            ).commit()
        }
    }

    class UiFragment : Fragment() {
        private var mWorkFragment: RetainedFragment? = null

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val v = inflater.inflate(R.layout.fragment_retain_instance, container, false)

            val button = v.findViewById<View>(R.id.restart) as Button
            button.setOnClickListener { mWorkFragment!!.restart() }

            return v
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            val fm = fragmentManager

            mWorkFragment = fm?.findFragmentByTag("work") as RetainedFragment?

            if (mWorkFragment == null) {
                mWorkFragment = RetainedFragment()
                mWorkFragment!!.setTargetFragment(this, 0)
                fm!!.beginTransaction().add(mWorkFragment!!, "work").commit()
            }
        }

    }

    class RetainedFragment : Fragment() {
        internal var mProgressBar: ProgressBar? = null
        internal var mPosition: Int = 0
        internal var mReady = false
        internal var mQuiting = false
        private val lock = Object()

        internal val mThread: Thread = object : Thread() {
            override fun run() {
                var max = 10000

                while (true) {

                    synchronized(lock) {
                        while (!mReady || mPosition >= max) {
                            if (mQuiting) {
                                return
                            }
                            try {
                                lock.wait()
                            } catch (e: InterruptedException) {
                            }

                        }

                        mPosition++
                        max = mProgressBar!!.max
                        mProgressBar!!.progress = mPosition
                    }

                    synchronized(lock) {
                        try {
                            lock.wait(50)
                        } catch (e: InterruptedException) {
                        }
                    }
                }
            }
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            retainInstance = true

            mThread.start()
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            mProgressBar = targetFragment?.view!!.findViewById<View>(
                R.id.progress_horizontal
            ) as ProgressBar

            synchronized(lock) {
                mReady = true
                lock.notify()
            }
        }

        override fun onDestroy() {
            synchronized(lock) {
                mReady = false
                mQuiting = true
                lock.notify()
            }

            super.onDestroy()
        }

        override fun onDetach() {
            synchronized(lock) {
                mProgressBar = null
                mReady = false
                lock.notify()
            }

            super.onDetach()
        }

        fun restart() {
            synchronized(lock) {
                mPosition = 0
                lock.notify()
            }
        }
    }
}