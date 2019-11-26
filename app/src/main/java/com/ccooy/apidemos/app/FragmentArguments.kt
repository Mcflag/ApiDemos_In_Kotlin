package com.ccooy.apidemos.app

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ccooy.apidemos.R

class FragmentArguments : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_arguments)

        if (savedInstanceState == null) {
            val ft = supportFragmentManager.beginTransaction()
            val newFragment = MyFragment.newInstance("From Arguments")
            ft.add(R.id.created, newFragment)
            ft.commit()
        }
    }

    class MyFragment : Fragment() {
        private var mLabel: CharSequence? = null

        override fun onInflate(
            context: Context,
            attrs: AttributeSet,
            savedInstanceState: Bundle?
        ) {
            super.onInflate(context, attrs, savedInstanceState)

            val a = context.obtainStyledAttributes(
                attrs,
                R.styleable.FragmentArguments
            )
            mLabel = a.getText(R.styleable.FragmentArguments_android_label)
            a.recycle()
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            val args = arguments
            if (args != null) {
                mLabel = args.getCharSequence("label", mLabel)
            }
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val v = inflater.inflate(R.layout.hello_world, container, false)
            val tv = v.findViewById<View>(R.id.text)
            (tv as TextView).text = if (mLabel != null) mLabel else "(no label)"
            tv.setBackground(resources.getDrawable(android.R.drawable.gallery_thumb))
            return v
        }

        companion object {
            internal fun newInstance(label: CharSequence): MyFragment {
                val f = MyFragment()
                val b = Bundle()
                b.putCharSequence("label", label)
                f.arguments = b
                return f
            }
        }
    }
}