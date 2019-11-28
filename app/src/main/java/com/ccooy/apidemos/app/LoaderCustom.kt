package com.ccooy.apidemos.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.ListFragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import com.ccooy.apidemos.R
import java.io.File
import java.text.Collator
import java.util.*

class LoaderCustom : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fm = supportFragmentManager

        if (fm.findFragmentById(android.R.id.content) == null) {
            val list = AppListFragment()
            fm.beginTransaction().add(android.R.id.content, list).commit()
        }
    }

    class AppEntry(private val mLoader: AppListLoader, val applicationInfo: ApplicationInfo) {

        val icon: Drawable?
            get() {
                if (mIcon == null) {
                    if (mApkFile.exists()) {
                        mIcon = applicationInfo.loadIcon(mLoader.mPm)
                        return mIcon
                    } else {
                        mMounted = false
                    }
                } else if (!mMounted) {
                    if (mApkFile.exists()) {
                        mMounted = true
                        mIcon = applicationInfo.loadIcon(mLoader.mPm)
                        return mIcon
                    }
                } else {
                    return mIcon
                }

                return mLoader.context.resources.getDrawable(
                    android.R.drawable.sym_def_app_icon
                )
            }

        private val mApkFile: File = File(applicationInfo.sourceDir)

        var label: String? = null
            private set

        private var mIcon: Drawable? = null
        private var mMounted: Boolean = false

        override fun toString(): String {
            return label ?: ""
        }

        internal fun loadLabel(context: Context) {
            if (label == null || !mMounted) {
                if (!mApkFile.exists()) {
                    mMounted = false
                    label = applicationInfo.packageName
                } else {
                    mMounted = true
                    val label = applicationInfo.loadLabel(context.packageManager)
                    this.label = label?.toString() ?: applicationInfo.packageName
                }
            }
        }
    }

    class InterestingConfigChanges {
        private val mLastConfiguration = Configuration()
        private var mLastDensity: Int = 0

        internal fun applyNewConfig(res: Resources): Boolean {
            val configChanges = mLastConfiguration.updateFrom(res.configuration)
            val densityChanged = mLastDensity != res.displayMetrics.densityDpi
            if (densityChanged || configChanges and (ActivityInfo.CONFIG_LOCALE
                        or ActivityInfo.CONFIG_UI_MODE or ActivityInfo.CONFIG_SCREEN_LAYOUT) != 0
            ) {
                mLastDensity = res.displayMetrics.densityDpi
                return true
            }
            return false
        }
    }

    class PackageIntentReceiver(private val mLoader: AppListLoader) : BroadcastReceiver() {

        init {
            val filter = IntentFilter(Intent.ACTION_PACKAGE_ADDED)
            filter.addAction(Intent.ACTION_PACKAGE_REMOVED)
            filter.addAction(Intent.ACTION_PACKAGE_CHANGED)
            filter.addDataScheme("package")
            mLoader.context.registerReceiver(this, filter)
            val sdFilter = IntentFilter()
            sdFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE)
            sdFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE)
            mLoader.context.registerReceiver(this, sdFilter)
        }

        override fun onReceive(context: Context, intent: Intent) {
            mLoader.onContentChanged()
        }
    }

    class AppListLoader(context: Context) : AsyncTaskLoader<List<AppEntry>>(context) {
        private val mLastConfig = InterestingConfigChanges()
        internal val mPm: PackageManager = getContext().packageManager

        private var mApps: List<AppEntry>? = null
        private var mPackageObserver: PackageIntentReceiver? = null

        override fun loadInBackground(): List<AppEntry> {
            var apps: List<ApplicationInfo>? = mPm.getInstalledApplications(
                PackageManager.MATCH_UNINSTALLED_PACKAGES or PackageManager.MATCH_DISABLED_COMPONENTS
            )
            if (apps == null) {
                apps = ArrayList()
            }

            val context = context

            val entries = ArrayList<AppEntry>(apps.size)
            for (i in apps.indices) {
                val entry = AppEntry(this, apps[i])
                entry.loadLabel(context)
                entries.add(entry)
            }

            Collections.sort(entries, ALPHA_COMPARATOR)

            return entries
        }

        override fun deliverResult(apps: List<AppEntry>?) {
            if (isReset) {
                if (apps != null) {
                    onReleaseResources(apps)
                }
            }
            val oldApps = mApps
            mApps = apps

            if (isStarted) {
                super.deliverResult(apps)
            }

            if (oldApps != null) {
                onReleaseResources(oldApps)
            }
        }

        override fun onStartLoading() {
            if (mApps != null) {
                deliverResult(mApps)
            }

            if (mPackageObserver == null) {
                mPackageObserver = PackageIntentReceiver(this)
            }

            val configChange = mLastConfig.applyNewConfig(context.resources)

            if (takeContentChanged() || mApps == null || configChange) {
                forceLoad()
            }
        }

        override fun onStopLoading() {
            cancelLoad()
        }

        override fun onCanceled(apps: List<AppEntry>?) {
            super.onCanceled(apps)

            onReleaseResources(apps)
        }

        override fun onReset() {
            super.onReset()

            onStopLoading()

            if (mApps != null) {
                onReleaseResources(mApps!!)
                mApps = null
            }

            if (mPackageObserver != null) {
                context.unregisterReceiver(mPackageObserver)
                mPackageObserver = null
            }
        }

        private fun onReleaseResources(apps: List<AppEntry>?) {
            // For a simple List<> there is nothing to do.  For something
            // like a Cursor, we would close it here.
        }
    }


    class AppListAdapter(context: Context) :
        ArrayAdapter<AppEntry>(context, android.R.layout.simple_list_item_2) {

        private val mInflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        fun setData(data: List<AppEntry>?) {
            clear()
            if (data != null) {
                addAll(data)
            }
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view: View

            if (convertView == null) {
                view = mInflater.inflate(R.layout.list_item_icon_text, parent, false)
            } else {
                view = convertView
            }

            val item = getItem(position)
            (view.findViewById<View>(R.id.icon) as ImageView).setImageDrawable(item!!.icon)
            (view.findViewById<View>(R.id.text) as TextView).text = item.label

            return view
        }
    }

    class AppListFragment : ListFragment(), SearchView.OnQueryTextListener,
        SearchView.OnCloseListener, LoaderManager.LoaderCallbacks<List<AppEntry>> {

        private lateinit var mAdapter: AppListAdapter

        private lateinit var mSearchView: SearchView

        private var mCurFilter: String? = null

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            setEmptyText("No applications")

            setHasOptionsMenu(true)

            mAdapter = AppListAdapter(activity as Context)
            listAdapter = mAdapter

            setListShown(false)

            loaderManager.initLoader(0, null, this)
        }

        class MySearchView(context: Context) : SearchView(context) {
            override fun onActionViewCollapsed() {
                setQuery("", false)
                super.onActionViewCollapsed()
            }
        }

        override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
            val item = menu.add("Search")
            item.setIcon(android.R.drawable.ic_menu_search)
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM or MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW)
            mSearchView = MySearchView(activity as Context)
            mSearchView.setOnQueryTextListener(this)
            mSearchView.setOnCloseListener(this)
            mSearchView.setIconifiedByDefault(true)
            item.actionView = mSearchView
        }

        override fun onQueryTextChange(newText: String): Boolean {
            mCurFilter = if (!TextUtils.isEmpty(newText)) newText else null
            mAdapter.filter.filter(mCurFilter)
            return true
        }

        override fun onQueryTextSubmit(query: String): Boolean {
            return true
        }

        override fun onClose(): Boolean {
            if (!TextUtils.isEmpty(mSearchView.query)) {
                mSearchView.setQuery(null, true)
            }
            return true
        }

        override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
            Log.i("LoaderCustom", "Item clicked: $id")
        }

        override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<AppEntry>> {
            return AppListLoader(activity as Context)
        }

        override fun onLoadFinished(loader: Loader<List<AppEntry>>, data: List<AppEntry>) {
            mAdapter.setData(data)

            if (isResumed) {
                setListShown(true)
            } else {
                setListShownNoAnimation(true)
            }
        }

        override fun onLoaderReset(loader: Loader<List<AppEntry>>) {
            mAdapter.setData(null)
        }
    }

    companion object {
        val ALPHA_COMPARATOR: Comparator<AppEntry> = object : Comparator<AppEntry> {
            private val sCollator = Collator.getInstance()
            override fun compare(object1: AppEntry, object2: AppEntry): Int {
                return sCollator.compare(object1.label, object2.label)
            }
        }
    }

}