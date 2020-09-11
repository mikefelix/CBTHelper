package com.mozzarelly.cbthelper

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var dao: EntryDao

    private lateinit var viewModel: EntriesViewModel

    private val viewModelProvider = viewModelProviderFactory {
        EntriesViewModel(dao)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        dao = CBTDatabase.getDatabase(this).entryDao()
/*
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }
*/
        viewModel = viewModelProvider.get()

        observe(viewModel.incompleteEntry){
            continueButton.visible = it != null
        }

        addButton.setOnClickListener {
            if (continueButton.visible) {
                doAfterConfirm(R.string.discardConfirmMsg) {
                    startActivity(Intent(this, AddEntryActivity::class.java))
                }
            }
            else {
                startActivity(Intent(this, AddEntryActivity::class.java))
            }
        }

        continueButton.setOnClickListener {
            startActivity(Intent(this, AddEntryActivity::class.java)
                .putExtra("continue", "true"))
        }

        viewButton.setOnClickListener {
            startActivity(Intent(this, ViewEntriesActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_settings -> startActivity(Intent(this, SettingsActivity::class.java))
            R.id.action_reminder -> supportFragmentManager.show(TimePickerFragment(this))

            else -> error("Unknown menu item")
        }

        return true
    }
}

fun FragmentManager?.show(fragment: DialogFragment){
    this ?: return
    fragment.show(this, "fragTag")
}