package com.mozzarelly.cbthelper

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.mozzarelly.cbthelper.editentry.AddEntryActivity
import com.mozzarelly.cbthelper.viewentries.EntriesViewModel
import com.mozzarelly.cbthelper.viewentries.ViewEntriesActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

const val RequestCodeStartEntry = 1
const val RequestCodeContinueEntry = 2
const val RequestCodeViewEntries = 3

class MainActivity : CBTActivity<EntriesViewModel>() {

    private val dao: EntryDao by lazy { CBTDatabase.getEntryDao(applicationContext) }

    override val viewModel: EntriesViewModel by viewModels { viewModelProvider }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        viewModel.load()
/*
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }
*/
        addButton.setOnClickListener {
            lifecycleScope.launch {
                try {
                    viewModel.incompleteEntry.collect { entry ->
                        if (entry != null) {
    //                        withContext(Dispatchers.Main) {
                                presentChoice(R.string.discardConfirmMsg, choice1 = R.string.continueButton, choice2 = R.string.newEntryButton,
                                    choice1Action = {
                                        start<AddEntryActivity>(RequestCodeContinueEntry)
                                    },
                                    choice2Action = {
                                        start<AddEntryActivity>(RequestCodeStartEntry,"forceNew" to "true")
                                    }
                                )
    //                        }
                        }
                        else {
                            start<AddEntryActivity>(RequestCodeStartEntry)
                        }
                    }
                } catch (e: Exception) {
                    e.rethrowIfCancellation()
                    e.printStackTrace()
                }
            }
        }

        viewButton.setOnClickListener {
            start<ViewEntriesActivity>(RequestCodeViewEntries)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode){
            RequestCodeStartEntry, RequestCodeContinueEntry ->
                findViewById<View>(R.id.main).shortSnackbar(if (resultCode >= 0) "Entry saved." else "Entry could not be saved.")
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