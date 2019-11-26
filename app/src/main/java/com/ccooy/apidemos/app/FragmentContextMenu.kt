package com.ccooy.apidemos.app

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ccooy.apidemos.R

class FragmentContextMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val content = ContextMenuFragment()
        supportFragmentManager.beginTransaction().add(android.R.id.content, content).commit()
    }

    class ContextMenuFragment : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val root = inflater.inflate(R.layout.fragment_context_menu, container, false)
            registerForContextMenu(root.findViewById(R.id.long_press))
            return root
        }

        override fun onCreateContextMenu(
            menu: ContextMenu,
            v: View,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            super.onCreateContextMenu(menu, v, menuInfo)
            menu.add(Menu.NONE, R.id.a_item, Menu.NONE, "Menu A")
            menu.add(Menu.NONE, R.id.b_item, Menu.NONE, "Menu B")
        }

        override fun onContextItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.a_item -> {
                    Toast.makeText(activity, "Item 1a was chosen", Toast.LENGTH_SHORT).show()
                    return true
                }
                R.id.b_item -> {
                    Toast.makeText(activity, "Item 1b was chosen", Toast.LENGTH_SHORT).show()
                    return true
                }
            }
            return super.onContextItemSelected(item)
        }
    }
}