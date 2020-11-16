package com.mozzarelly.cbthelper

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.MainThread
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import com.mozzarelly.cbthelper.editentry.AddEntryActivity
import com.mozzarelly.cbthelper.viewentries.EntriesViewModel
import com.mozzarelly.cbthelper.viewentries.ViewEntriesActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.reflect.KClass

class MainActivity : CBTActivity<EntriesViewModel>() {

    override val layout = R.layout.activity_main
    override val viewModel: EntriesViewModel by cbtViewModel()

    override fun EntriesViewModel.setup() {
        load()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)

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

        viewButton.setOnClickListener {
            start<ViewEntriesActivity>()
        }
    }

    override val onReturnFrom = mapOf<KClass<*>, (Int) -> Unit>(
        AddEntryActivity::class to { it: Int ->
            findViewById<View>(R.id.main).shortSnackbar(if (it >= 0) "Entry saved." else "Entry could not be saved.")
        }
    )

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
