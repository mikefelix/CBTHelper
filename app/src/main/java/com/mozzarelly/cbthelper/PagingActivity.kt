@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.mozzarelly.cbthelper

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

abstract class PagingActivity<V: PagingViewModel> : CBTActivity<V>() {

    protected lateinit var fragments: List<Fragment>

    protected abstract val layout: Int
    protected abstract val fragmentTypes: List<KClass<out PageFragment<V>>>

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onBackPressed() {
        if (viewModel.currPage > 1)
            viewModel.previousPage()
        else
            super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)
        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        observe(viewModel.title) { title = it }

        setupFragments()
        setupPaging()
    }

    private fun setupPaging() {
        lifecycleScope.launch {
            try {
                viewModel.changingPage.collect { (newPage, anim) ->
                    if (newPage > 0) {
                        supportFragmentManager.runTx {
                            if (anim == -1)
                                setCustomAnimations(
                                    R.anim.enter_from_left,
                                    R.anim.exit_to_right,
                                    R.anim.enter_from_right,
                                    R.anim.exit_to_left
                                )
                            else if (anim == 1)
                                setCustomAnimations(
                                    R.anim.enter_from_right,
                                    R.anim.exit_to_left,
                                    R.anim.enter_from_left,
                                    R.anim.exit_to_right
                                )

                            replace(R.id.contentFragment, fragments[newPage - 1])
                        }
                    }
                }
            }
            catch (e: Exception) {
                e.rethrowIfCancellation()
                e.printStackTrace()
            }
        }
    }

    private fun setupFragments() {
        fragments = fragmentTypes.map { it.java.constructors[0].newInstance() as Fragment }

/*
        supportFragmentManager.runTx {
            for (i in fragments.size downTo 1) {
                val fragment = fragments[i - 1]
                add(R.id.contentFragment, fragment, "fragment_$i")
                if (i > 1)
                    hide(fragment)
            }
        }
*/
    }
/*
    inline fun <reified V: ViewModel> viewModelProviderFactory(crossinline setup: (V) -> Unit): ViewModelProvider.NewInstanceFactory {
        return object: ViewModelProvider.NewInstanceFactory() {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                acti
            }
        }
    }*/
}

