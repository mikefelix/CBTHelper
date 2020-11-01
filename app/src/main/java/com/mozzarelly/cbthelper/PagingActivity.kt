@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.mozzarelly.cbthelper

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

abstract class PagingActivity<V: PagingViewModel> : CBTActivity<V>() {

    protected lateinit var fragments: List<Fragment>

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

        observe(viewModel.title) { title = it }

        setupFragments()
        setupPaging()
    }

    private fun setupPaging() {
        lifecycleScope.launch {
            whenStarted {
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
    }

    private fun setupFragments() {
        fragments = fragmentTypes.map { it.java.constructors[0].newInstance() as Fragment }
    }

}

