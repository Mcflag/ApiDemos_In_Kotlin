package com.ccooy.apidemos.app

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.SearchView
import android.widget.SimpleCursorAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.ListFragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader

class LoaderRetained : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fm = supportFragmentManager

        if (fm.findFragmentById(android.R.id.content) == null) {
            val list = CursorLoaderListFragment()
            fm.beginTransaction().add(android.R.id.content, list).commit()
        }
    }

    class CursorLoaderListFragment : ListFragment(), SearchView.OnQueryTextListener,
        SearchView.OnCloseListener,
        LoaderManager.LoaderCallbacks<Cursor> {

        private lateinit var mAdapter: SimpleCursorAdapter

        private lateinit var mSearchView: SearchView

        private var mCurFilter: String? = null

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            retainInstance = true

            setEmptyText("No phone numbers")

            setHasOptionsMenu(true)

            mAdapter = SimpleCursorAdapter(
                activity,
                android.R.layout.simple_list_item_2, null,
                arrayOf(ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.CONTACT_STATUS),
                intArrayOf(android.R.id.text1, android.R.id.text2), 0
            )
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
            val newFilter = if (!TextUtils.isEmpty(newText)) newText else null
            if (mCurFilter == null && newFilter == null) {
                return true
            }
            if (mCurFilter != null && mCurFilter == newFilter) {
                return true
            }
            mCurFilter = newFilter
            loaderManager.restartLoader(0, null, this)
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
            Log.i("FragmentComplexList", "Item clicked: $id")
        }

        override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
            val baseUri: Uri
            if (mCurFilter != null) {
                baseUri = Uri.withAppendedPath(
                    ContactsContract.Contacts.CONTENT_FILTER_URI,
                    Uri.encode(mCurFilter)
                )
            } else {
                baseUri = ContactsContract.Contacts.CONTENT_URI
            }

            val select = ("((" + ContactsContract.Contacts.DISPLAY_NAME + " NOTNULL) AND ("
                    + ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1) AND ("
                    + ContactsContract.Contacts.DISPLAY_NAME + " != '' ))")
            return CursorLoader(
                activity as Context, baseUri,
                CONTACTS_SUMMARY_PROJECTION, select, null,
                ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC"
            )
        }

        override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
            mAdapter.swapCursor(data)
            if (isResumed) {
                setListShown(true)
            } else {
                setListShownNoAnimation(true)
            }
        }

        override fun onLoaderReset(loader: Loader<Cursor>) {
            mAdapter.swapCursor(null)
        }

        companion object {
            internal val CONTACTS_SUMMARY_PROJECTION = arrayOf(
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.CONTACT_STATUS,
                ContactsContract.Contacts.CONTACT_PRESENCE,
                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.Contacts.LOOKUP_KEY
            )
        }
    }

}