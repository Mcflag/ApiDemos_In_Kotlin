package com.ccooy.apidemos.app

import android.app.ListActivity
import android.content.Context
import android.database.CharArrayBuffer
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.view.ViewGroup
import android.widget.QuickContactBadge
import android.widget.ResourceCursorAdapter
import android.widget.TextView
import com.ccooy.apidemos.R

class QuickContactsDemo : ListActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val select = ("((" + ContactsContract.Contacts.DISPLAY_NAME + " NOTNULL) AND ("
                + ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1) AND ("
                + ContactsContract.Contacts.DISPLAY_NAME + " != '' ))")
        val c = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI, CONTACTS_SUMMARY_PROJECTION, select,
            null, ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC"
        )
        startManagingCursor(c)
        val adapter = ContactListItemAdapter(this, R.layout.quick_contacts, c)
        listAdapter = adapter

    }

    private inner class ContactListItemAdapter(context: Context, layout: Int, c: Cursor) :
        ResourceCursorAdapter(context, layout, c) {

        override fun bindView(view: View, context: Context, cursor: Cursor) {
            val cache = view.tag as ContactListItemCache
            // Set the name
            cursor.copyStringToBuffer(SUMMARY_NAME_COLUMN_INDEX, cache.nameBuffer)
            val size = cache.nameBuffer.sizeCopied
            cache.nameView!!.setText(cache.nameBuffer.data, 0, size)
            val contactId = cursor.getLong(SUMMARY_ID_COLUMN_INDEX)
            val lookupKey = cursor.getString(SUMMARY_LOOKUP_KEY)
            cache.photoView!!.assignContactUri(ContactsContract.Contacts.getLookupUri(contactId, lookupKey))
        }

        override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
            val view = super.newView(context, cursor, parent)
            val cache = ContactListItemCache()
            cache.nameView = view.findViewById(R.id.name) as TextView
            cache.photoView = view.findViewById(R.id.badge) as QuickContactBadge
            view.tag = cache

            return view
        }
    }

    internal class ContactListItemCache {
        var nameView: TextView? = null
        var photoView: QuickContactBadge? = null
        var nameBuffer = CharArrayBuffer(128)
    }

    companion object {
        internal val CONTACTS_SUMMARY_PROJECTION = arrayOf(
            ContactsContract.Contacts._ID, // 0
            ContactsContract.Contacts.DISPLAY_NAME, // 1
            ContactsContract.Contacts.STARRED, // 2
            ContactsContract.Contacts.TIMES_CONTACTED, // 3
            ContactsContract.Contacts.CONTACT_PRESENCE, // 4
            ContactsContract.Contacts.PHOTO_ID, // 5
            ContactsContract.Contacts.LOOKUP_KEY, // 6
            ContactsContract.Contacts.HAS_PHONE_NUMBER
        )// 7

        internal val SUMMARY_ID_COLUMN_INDEX = 0
        internal val SUMMARY_NAME_COLUMN_INDEX = 1
        internal val SUMMARY_STARRED_COLUMN_INDEX = 2
        internal val SUMMARY_TIMES_CONTACTED_COLUMN_INDEX = 3
        internal val SUMMARY_PRESENCE_STATUS_COLUMN_INDEX = 4
        internal val SUMMARY_PHOTO_ID_COLUMN_INDEX = 5
        internal val SUMMARY_LOOKUP_KEY = 6
        internal val SUMMARY_HAS_PHONE_COLUMN_INDEX = 7
    }
}