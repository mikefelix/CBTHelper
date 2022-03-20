@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.mozzarelly.cbthelper

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import com.mozzarelly.cbthelper.databinding.WebviewBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

abstract class InterviewActivity<V: InterviewViewModel> : CBTActivity<V>() {

    override val layout = R.layout.activity_interview

    protected lateinit var fragments: List<Fragment>

    protected abstract val fragmentTypes: List<KClass<out PageFragment<V>>>

    override fun onSupportNavigateUp(): Boolean {
        lifecycleScope.launch {
            val result = viewModel.saveAsync()
            setResult(result)
            finish()
        }
        return true
    }

    override fun onBackPressed() {
        if (viewModel.currPage > 1) {
            viewModel.previousPage()
        }
        else {
            lifecycleScope.launch {
                val result = viewModel.saveAsync()
                setResult(result)
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observe(viewModel.title) { title = it }

        setupFragments()
        setupPaging()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_interview, menu)
        return true
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_settings -> startActivity(Intent(this, SettingsActivity::class.java))
            R.id.action_about -> showAbout()
            R.id.action_read -> {
                showPatientGuide()
                /*val (headingText, explanationText) = viewModel.getReadingPage()
                showExpandedBottomSheet {
                    PopupBinding.inflate(layoutInflater, null, false).apply {
                        done.setOnClickListener { dismiss() }
                        heading.run {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                                justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD

                            text = headingText
                        }
                        text.run {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                                justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD

                            text = explanationText
                        }
                    }.root
                }*/
            }
            else -> error("Unknown menu item")
        }

        return true
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

