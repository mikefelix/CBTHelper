package com.mozzarelly.cbthelper

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.MainThread
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import com.mozzarelly.cbthelper.analyze.AnalyzeActivity
import com.mozzarelly.cbthelper.editentry.AddEntryActivity
import com.mozzarelly.cbthelper.viewentries.EntriesViewModel
import com.mozzarelly.cbthelper.viewentries.ViewEntriesActivity
import kotlin.reflect.KClass

class MainActivity : CBTActivity<EntriesViewModel>() {

    override val layout = R.layout.activity_main
    override val viewModel: EntriesViewModel by cbtViewModel()

    override fun EntriesViewModel.setup() {
        load()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)

        findViewById<TextView>(R.id.version).text = "Version " + BuildConfig.VERSION_NAME

//        addButton.setOnClickListener {
//            start<AddEntryActivity>()
//        }

        val addButton = findViewById<Button>(R.id.addButton)

        observe(viewModel.incompleteEntry){
            if (it == null) {
                addButton.setOnClickListener {
                    start<AddEntryActivity>()
                }
            }
            else {
                addButton.setOnClickListener {
                    presentChoice(R.string.discardConfirmMsg,
                        choice1 = R.string.continueButton,
                        choice2 = R.string.newEntryButton,
                        choice1Action = {
                            start<AddEntryActivity>()
                        },
                        choice2Action = {
                            start<AddEntryActivity>("forceNew" to "true")
                        }
                    )
                }
            }
        }

        findViewById<Button>(R.id.viewButton).run {
            observe(viewModel.allEntries){
                this.visibility = if (visible) View.VISIBLE else View.GONE
            }

            setOnClickListener {
                start<ViewEntriesActivity>()
            }
        }
    }

    override val onReturnFrom = mapOf<KClass<*>, (Int) -> Unit>(
        AddEntryActivity::class to { result ->
            showSavedEntryDialog(result)
        }
    )

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_settings -> startActivity(Intent(this, SettingsActivity::class.java))
            R.id.action_about -> showAbout()
            R.id.action_reminder -> supportFragmentManager.show(TimePickerFragment(this))
            R.id.action_delete -> AlertDialog.Builder(this)
                .setTitle("Delete all data")
                .setMessage("Delete all your data? This cannot be undone!")
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    viewModel.cleanDatabase()
                }
                .setNegativeButton(android.R.string.cancel) { _, _ -> }
                .show()

            else -> error("Unknown menu item")
        }

        return true
    }
}

fun Context.showAbout(): Boolean {
    AlertDialog.Builder(this).apply {
        setTitle("CBT Helper")
        setMessage(R.string.about)
    }.show()

    return true
}

fun FragmentManager?.show(fragment: DialogFragment){
    this ?: return
    fragment.show(this, "fragTag")
}

@MainThread
inline fun <reified VM : CBTViewModel> CBTActivity<VM>.cbtViewModel(): Lazy<VM> =
    ViewModelLazy(VM::class, { viewModelStore }, { object: ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.newInstance().also {
                (it as VM).run {
                    applicationContext = this@cbtViewModel.applicationContext
                    setup()
                }
            }
        }
    } })
