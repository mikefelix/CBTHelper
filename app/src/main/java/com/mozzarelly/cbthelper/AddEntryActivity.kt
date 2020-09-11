package com.mozzarelly.cbthelper

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_add_entry.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AddEntryActivity : AppCompatActivity() {

    private val fragmentTypes = listOf(
        AddEntry1Fragment::class,
        AddEntry2Fragment::class,
        AddEntry3Fragment::class,
        AddEntry4Fragment::class,
        AddEntry5Fragment::class,
        AddEntry6Fragment::class
    )

    private lateinit var viewModel: EditEntryViewModel

    private val dao: EntryDao by lazy { CBTDatabase.getEntryDao(this) }

    private val viewModelProvider = viewModelProviderFactory {
        EditEntryViewModel(fragmentTypes.size, dao)
    }

    private lateinit var fragments: List<Fragment>

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_entry)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        setupViewModel()
        setupFragments()

        observe(viewModel.page) { (page, _) ->
            title = if (viewModel.completeValue.value != true)
                "Add entry - ${page}/${fragments.size}"
            else
                "View completed entry"
        }

        GlobalScope.launch {
            try {
                viewModel.changingPage.collect { (newPage, anim) ->
                    if (newPage > 0){
                        supportFragmentManager.run {
                            if (anim == -1)
                                setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left)
                            else if (anim == 1)
                                setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)

                            replace(R.id.contentFragment, fragments[newPage - 1])
                        }
                    }
                }
            }
            catch (e: CancellationException) {
                throw e
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setupViewModel(){
        viewModel = viewModelProvider.get<EditEntryViewModel>().apply {
            when {
                intent.hasExtra("continue") -> loadEntryInProgress()
                intent.hasExtra("id") -> loadEntry(intent.getIntExtra("id", 0))
                else -> loadNewEntry()
            }
        }
    }

    private fun setupFragments(){
        fragments = fragmentTypes.map { it.java.constructors[0].newInstance(viewModel) as Fragment }

        supportFragmentManager.run {
//            for (i in 1..fragments.size) {
            for (i in fragments.size downTo 1) {
                val fragment = fragments[i - 1]
                add(R.id.contentFragment, fragment, "fragment_$i")
            }
        }
    }
}

inline fun <reified V: ViewModel> viewModelProviderFactory(crossinline const: () -> V): ViewModelProvider.NewInstanceFactory {
    return object: ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return const() as T
        }
    }
}
