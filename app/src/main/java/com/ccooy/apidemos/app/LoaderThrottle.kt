package com.ccooy.apidemos.app

import android.content.Context
import android.content.ContentUris
import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.BaseColumns
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.ListFragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import java.util.HashMap

class LoaderThrottle : AppCompatActivity() {

    class MainTable private constructor() : BaseColumns {
        companion object {
            const val TABLE_NAME = "main"

            val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/main")

            val CONTENT_ID_URI_BASE: Uri = Uri.parse("content://$AUTHORITY/main/")

            const val CONTENT_TYPE = "vnd.android.cursor.dir/vnd.example.api-demos-throttle"

            const val CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.example.api-demos-throttle"

            const val DEFAULT_SORT_ORDER = "data COLLATE LOCALIZED ASC"

            const val COLUMN_NAME_DATA = "data"
        }
    }

    internal class DatabaseHelper(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(
                "CREATE TABLE " + MainTable.TABLE_NAME + " ("
                        + BaseColumns._ID + " INTEGER PRIMARY KEY,"
                        + MainTable.COLUMN_NAME_DATA + " TEXT"
                        + ");"
            )
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            Log.w(
                TAG, "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data"
            )

            db.execSQL("DROP TABLE IF EXISTS notes")

            onCreate(db)
        }

        companion object {
            private const val DATABASE_NAME = "loader_throttle.db"
            private const val DATABASE_VERSION = 2
        }
    }

    class SimpleProvider : ContentProvider() {
        private val mNotesProjectionMap: HashMap<String, String> = HashMap()
        private val mUriMatcher: UriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        private var mOpenHelper: DatabaseHelper? = null

        init {
            mUriMatcher.addURI(AUTHORITY, MainTable.TABLE_NAME, MAIN)
            mUriMatcher.addURI(AUTHORITY, MainTable.TABLE_NAME + "/#", MAIN_ID)

            mNotesProjectionMap[BaseColumns._ID] = BaseColumns._ID
            mNotesProjectionMap[MainTable.COLUMN_NAME_DATA] = MainTable.COLUMN_NAME_DATA
        }

        override fun onCreate(): Boolean {
            mOpenHelper = DatabaseHelper(context)
            return true
        }

        override fun query(
            uri: Uri, projection: Array<String>?, selection: String?,
            mSelectionArgs: Array<String>?, mSortOrder: String?
        ): Cursor? {
            var selectionArgs = mSelectionArgs
            var sortOrder = mSortOrder

            val qb = SQLiteQueryBuilder()
            qb.tables = MainTable.TABLE_NAME

            when (mUriMatcher.match(uri)) {
                MAIN ->
                    qb.setProjectionMap(mNotesProjectionMap)

                MAIN_ID -> {
                    qb.setProjectionMap(mNotesProjectionMap)
                    qb.appendWhere(BaseColumns._ID + "=?")
                    selectionArgs = DatabaseUtils.appendSelectionArgs(
                        selectionArgs,
                        arrayOf(uri.lastPathSegment)
                    )
                }

                else -> throw IllegalArgumentException("Unknown URI $uri")
            }


            if (TextUtils.isEmpty(sortOrder)) {
                sortOrder = MainTable.DEFAULT_SORT_ORDER
            }

            val db = mOpenHelper!!.readableDatabase

            val c = qb.query(
                db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
            )

            c.setNotificationUri(context!!.contentResolver, uri)
            return c
        }

        override fun getType(uri: Uri): String? {
            return when (mUriMatcher.match(uri)) {
                MAIN -> MainTable.CONTENT_TYPE
                MAIN_ID -> MainTable.CONTENT_ITEM_TYPE
                else -> throw IllegalArgumentException("Unknown URI $uri")
            }
        }

        override fun insert(uri: Uri, initialValues: ContentValues?): Uri? {
            require(mUriMatcher.match(uri) == MAIN) {
                "Unknown URI $uri"
            }

            val values: ContentValues = if (initialValues != null) {
                ContentValues(initialValues)
            } else {
                ContentValues()
            }

            if (!values.containsKey(MainTable.COLUMN_NAME_DATA)) {
                values.put(MainTable.COLUMN_NAME_DATA, "")
            }

            val db = mOpenHelper!!.writableDatabase

            val rowId = db.insert(MainTable.TABLE_NAME, null, values)

            if (rowId > 0) {
                val noteUri = ContentUris.withAppendedId(MainTable.CONTENT_ID_URI_BASE, rowId)
                context!!.contentResolver.notifyChange(noteUri, null)
                return noteUri
            }

            throw SQLException("Failed to insert row into $uri")
        }

        override fun delete(uri: Uri, where: String?, whereArgs: Array<String>?): Int {
            val db = mOpenHelper!!.writableDatabase
            val finalWhere: String

            val count: Int

            when (mUriMatcher.match(uri)) {
                MAIN ->
                    count = db.delete(MainTable.TABLE_NAME, where, whereArgs)

                MAIN_ID -> {
                    finalWhere = DatabaseUtils.concatenateWhere(
                        BaseColumns._ID + " = " + ContentUris.parseId(uri), where
                    )
                    count = db.delete(MainTable.TABLE_NAME, finalWhere, whereArgs)
                }

                else -> throw IllegalArgumentException("Unknown URI $uri")
            }

            context!!.contentResolver.notifyChange(uri, null)

            return count
        }

        override fun update(
            uri: Uri,
            values: ContentValues?,
            where: String?,
            whereArgs: Array<String>?
        ): Int {
            val db = mOpenHelper!!.writableDatabase
            val count: Int
            val finalWhere: String

            when (mUriMatcher.match(uri)) {
                MAIN ->
                    count = db.update(MainTable.TABLE_NAME, values, where, whereArgs)

                MAIN_ID -> {
                    finalWhere = DatabaseUtils.concatenateWhere(
                        BaseColumns._ID + " = " + ContentUris.parseId(uri), where
                    )
                    count = db.update(MainTable.TABLE_NAME, values, finalWhere, whereArgs)
                }

                else -> throw IllegalArgumentException("Unknown URI $uri")
            }

            context!!.contentResolver.notifyChange(uri, null)

            return count
        }

        companion object {
            private const val MAIN = 1
            private const val MAIN_ID = 2
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fm = supportFragmentManager

        if (fm.findFragmentById(android.R.id.content) == null) {
            val list = ThrottledLoaderListFragment()
            fm.beginTransaction().add(android.R.id.content, list).commit()
        }
    }

    class ThrottledLoaderListFragment : ListFragment(), LoaderManager.LoaderCallbacks<Cursor> {
        private lateinit var mAdapter: SimpleCursorAdapter

        internal var mCurFilter: String? = null

        private var mPopulatingTask: AsyncTask<Void, Void, Void>? = null

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            setEmptyText("No data.  Select 'Populate' to fill with data from Z to A at a rate of 4 per second.")
            setHasOptionsMenu(true)

            mAdapter = SimpleCursorAdapter(
                activity,
                android.R.layout.simple_list_item_1, null,
                arrayOf(MainTable.COLUMN_NAME_DATA),
                intArrayOf(android.R.id.text1), 0
            )
            listAdapter = mAdapter

            setListShown(false)

            loaderManager.initLoader(0, null, this)
        }

        override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
            menu.add(Menu.NONE, POPULATE_ID, 0, "Populate")
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            menu.add(Menu.NONE, CLEAR_ID, 0, "Clear")
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            val cr = activity?.contentResolver

            when (item.itemId) {
                POPULATE_ID -> {
                    if (mPopulatingTask != null) {
                        mPopulatingTask!!.cancel(false)
                    }
                    mPopulatingTask = object : AsyncTask<Void, Void, Void>() {
                        override fun doInBackground(vararg params: Void): Void? {
                            var c = 'Z'
                            while (c >= 'A') {
                                if (isCancelled) {
                                    break
                                }
                                val builder = StringBuilder("Data ")
                                builder.append(c)
                                val values = ContentValues()
                                values.put(MainTable.COLUMN_NAME_DATA, builder.toString())
                                cr?.insert(MainTable.CONTENT_URI, values)
                                // Wait a bit between each insert.
                                try {
                                    Thread.sleep(250)
                                } catch (e: InterruptedException) {
                                }

                                c--
                            }
                            return null
                        }
                    }
                    mPopulatingTask!!.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null)
                    return true
                }

                CLEAR_ID -> {
                    if (mPopulatingTask != null) {
                        mPopulatingTask!!.cancel(false)
                        mPopulatingTask = null
                    }
                    val task = object : AsyncTask<Void, Void, Void>() {
                        override fun doInBackground(vararg params: Void): Void? {
                            cr?.delete(MainTable.CONTENT_URI, null, null)
                            return null
                        }
                    }
                    task.execute(null)
                    return true
                }

                else -> return super.onOptionsItemSelected(item)
            }
        }

        override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
            Log.i(TAG, "Item clicked: $id")
        }

        override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
            val cl = CursorLoader(
                activity as Context, MainTable.CONTENT_URI,
                PROJECTION, null, null, null
            )
            cl.setUpdateThrottle(2000)
            return cl
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
            internal const val POPULATE_ID = Menu.FIRST
            internal const val CLEAR_ID = Menu.FIRST + 1

            internal val PROJECTION = arrayOf(BaseColumns._ID, MainTable.COLUMN_NAME_DATA)
        }
    }

    companion object {
        internal const val TAG = "LoaderThrottle"
        const val AUTHORITY = "com.ccooy.apidemos.app.LoaderThrottle"
    }
}