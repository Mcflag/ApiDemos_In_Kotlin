package com.ccooy.apidemos.app

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.ListFragment
import com.ccooy.apidemos.R
import com.ccooy.apidemos.Shakespeare

class FragmentLayout : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_layout)
    }

    class DetailsActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                finish()
                return
            }

            if (savedInstanceState == null) {
                val details = DetailsFragment()
                details.arguments = intent.extras
                supportFragmentManager.beginTransaction().add(android.R.id.content, details)
                    .commit()
            }
        }
    }

    class TitlesFragment : ListFragment() {
        private var mDualPane: Boolean = false
        private var mCurCheckPosition = 0

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            listAdapter = ArrayAdapter(
                activity,
                android.R.layout.simple_list_item_activated_1,
                Shakespeare.TITLES
            )

            val detailsFrame = activity?.findViewById<View>(R.id.details)
            mDualPane = detailsFrame != null && detailsFrame.visibility == View.VISIBLE

            if (savedInstanceState != null) {
                mCurCheckPosition = savedInstanceState.getInt("curChoice", 0)
            }

            if (mDualPane) {
                listView.choiceMode = ListView.CHOICE_MODE_SINGLE
                showDetails(mCurCheckPosition)
            }
        }

        override fun onSaveInstanceState(outState: Bundle) {
            super.onSaveInstanceState(outState)
            outState.putInt("curChoice", mCurCheckPosition)
        }

        override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
            showDetails(position)
        }

        private fun showDetails(index: Int) {
            mCurCheckPosition = index

            if (mDualPane) {
                listView.setItemChecked(index, true)

                var details: DetailsFragment? =
                    fragmentManager?.findFragmentById(R.id.details) as DetailsFragment?
                if (details == null || details.shownIndex != index) {
                    details = DetailsFragment.newInstance(index)

                    val ft = fragmentManager?.beginTransaction()
                    ft?.replace(R.id.details, details)
                    ft?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    ft?.commit()
                }
            } else {
                val intent = Intent()
                intent.setClass(activity, DetailsActivity::class.java)
                intent.putExtra("index", index)
                startActivity(intent)
            }
        }
    }

    class DetailsFragment : Fragment() {

        val shownIndex: Int
            get() = arguments?.getInt("index", 0) ?: 0

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            if (container == null) {
                return null
            }

            val scroller = ScrollView(activity)
            val text = TextView(activity)
            val padding = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                4f,
                activity?.resources?.displayMetrics
            ).toInt()
            text.setPadding(padding, padding, padding, padding)
            scroller.addView(text)
            text.text = Shakespeare.DIALOGUE[shownIndex]
            return scroller
        }

        companion object {
            fun newInstance(index: Int): DetailsFragment {
                val f = DetailsFragment()
                val args = Bundle()
                args.putInt("index", index)
                f.arguments = args

                return f
            }
        }
    }
}