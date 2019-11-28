package com.ccooy.apidemos.app

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TabHost
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.ccooy.apidemos.R
import java.util.ArrayList

class FragmentTabsFragment : Fragment() {
    private lateinit var mTabManager: TabManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mTabManager = TabManager(
            activity as Context, childFragmentManager,
            R.id.realtabcontent
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_tabs_fragment, container, false)
        val host = mTabManager.handleCreateView(v)

        mTabManager.addTab(
            host.newTabSpec("result").setIndicator("Result"),
            FragmentReceiveResult.ReceiveResultFragment::class.java, null
        )
        mTabManager.addTab(
            host.newTabSpec("contacts").setIndicator("Contacts"),
            LoaderCursor.CursorLoaderListFragment::class.java, null
        )
        mTabManager.addTab(
            host.newTabSpec("apps").setIndicator("Apps"),
            LoaderCustom.AppListFragment::class.java, null
        )
        mTabManager.addTab(
            host.newTabSpec("throttle").setIndicator("Throttle"),
            LoaderThrottle.ThrottledLoaderListFragment::class.java, null
        )

        return v
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        mTabManager.handleViewStateRestored(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mTabManager.handleDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mTabManager.handleSaveInstanceState(outState)
    }

    class TabManager(
        private val mContext: Context,
        private val mManager: FragmentManager,
        private val mContainerId: Int
    ) : TabHost.OnTabChangeListener {
        private val mTabs = ArrayList<TabInfo>()
        private var mTabHost: TabHost? = null
        private var mLastTab: TabInfo? = null
        private var mInitialized: Boolean = false
        private var mCurrentTabTag: String? = null

        internal class TabInfo(
            internal val tag: String,
            internal val clss: Class<*>,
            internal val args: Bundle?
        ) {
            internal var fragment: Fragment? = null
        }

        internal class DummyTabFactory(private val mContext: Context) : TabHost.TabContentFactory {

            override fun createTabContent(tag: String): View {
                val v = View(mContext)
                v.minimumWidth = 0
                v.minimumHeight = 0
                return v
            }
        }

        fun handleCreateView(root: View): TabHost {
            check(mTabHost == null) { "TabHost already set" }
            mTabHost = root.findViewById<View>(android.R.id.tabhost) as TabHost
            mTabHost!!.setup()
            mTabHost!!.setOnTabChangedListener(this)
            return mTabHost!!
        }

        fun addTab(tabSpec: TabHost.TabSpec, clss: Class<*>, args: Bundle?) {
            tabSpec.setContent(DummyTabFactory(mContext))
            val tag = tabSpec.tag
            val info = TabInfo(tag, clss, args)
            mTabs.add(info)
            mTabHost!!.addTab(tabSpec)
        }

        fun handleViewStateRestored(savedInstanceState: Bundle?) {
            if (savedInstanceState != null) {
                mCurrentTabTag = savedInstanceState.getString("tab")
            }
            mTabHost!!.setCurrentTabByTag(mCurrentTabTag)

            val currentTab = mTabHost!!.currentTabTag

            var ft: FragmentTransaction? = null
            for (i in mTabs.indices) {
                val tab = mTabs[i]
                tab.fragment = mManager.findFragmentByTag(tab.tag)
                if (tab.fragment != null && !tab.fragment!!.isDetached) {
                    if (tab.tag == currentTab) {
                        mLastTab = tab
                    } else {
                        if (ft == null) {
                            ft = mManager.beginTransaction()
                        }
                        ft!!.detach(tab.fragment!!)
                    }
                }
            }

            mInitialized = true
            ft = doTabChanged(currentTab, ft)
            if (ft != null) {
                ft.commit()
                mManager.executePendingTransactions()
            }
        }

        fun handleDestroyView() {
            mCurrentTabTag = mTabHost!!.currentTabTag
            mTabHost = null
            mTabs.clear()
            mInitialized = false
        }

        fun handleSaveInstanceState(outState: Bundle) {
            outState.putString(
                "tab", if (mTabHost != null)
                    mTabHost!!.currentTabTag
                else
                    mCurrentTabTag
            )
        }

        override fun onTabChanged(tabId: String) {
            if (!mInitialized) {
                return
            }
            val ft = doTabChanged(tabId, null)
            ft?.commit()
        }

        private fun doTabChanged(tabId: String, ft: FragmentTransaction?): FragmentTransaction? {
            var ft = ft
            var newTab: TabInfo? = null
            for (i in mTabs.indices) {
                val tab = mTabs[i]
                if (tab.tag == tabId) {
                    newTab = tab
                }
            }
            checkNotNull(newTab) { "No tab known for tag $tabId" }
            if (mLastTab != newTab) {
                if (ft == null) {
                    ft = mManager.beginTransaction()
                }
                if (mLastTab != null) {
                    if (mLastTab!!.fragment != null) {
                        ft!!.detach(mLastTab!!.fragment!!)
                    }
                }
                if (newTab != null) {
                    if (newTab.fragment == null) {
                        newTab.fragment = Fragment.instantiate(
                            mContext,
                            newTab.clss.name, newTab.args
                        )
                        ft!!.add(mContainerId, newTab.fragment!!, newTab.tag)
                    } else {
                        ft!!.attach(newTab.fragment!!)
                    }
                }

                mLastTab = newTab
            }
            return ft
        }
    }
}