package com.mozzarelly.cbthelper.analyze

import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.mozzarelly.cbthelper.*
import com.mozzarelly.cbthelper.behavior.BehaviorActivity
import com.mozzarelly.cbthelper.cogvalid.CogValidActivity
import com.mozzarelly.cbthelper.replacement.ReplacementThoughtsActivity
import kotlin.reflect.KClass

class AnalyzeActivity : CBTActivity<AnalyzeViewModel>() {

    override val layout: Int = R.layout.activity_analyze

    override val viewModel: AnalyzeViewModel by cbtViewModel()

    var stage = Stage.Begun
        set(value){
            if (field != value) {
                val frag = when (value) {
                    Stage.BehaviorComplete -> AnalyzeBehaviorSummaryFragment()
                    Stage.RatRepComplete -> AnalyzeRatRepSummaryFragment()
                    Stage.CogValComplete -> AnalyzeCogValidSummaryFragment()
                    Stage.EntryComplete -> AnalyzeEntrySummaryFragment()
                    Stage.Begun -> AnalyzeEntrySummaryFragment()
                }

                show(frag)

                field = value
            }
        }

    override val onReturnFrom: Map<KClass<*>, (Int) -> Unit> = mapOf<KClass<*>, (Int) -> Unit>(
        BehaviorActivity::class to { it: Int ->
            snackbar(if (it >= 0) "Behavior analysis saved." else "Behavior analysis could not be saved.")
        },
        ReplacementThoughtsActivity::class to { it: Int ->
            snackbar(if (it >= 0) "Replacement thoughts results saved." else "Replacement thoughts results could not be saved.")
        },
        CogValidActivity::class to { it: Int ->
            snackbar(if (it >= 0) "Cognition validity results saved." else "Cognition validity results could not be saved.")
        }
    )

    override fun AnalyzeViewModel.setup() {
        id = getIdExtra()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = "Summary"

        observe(viewModel.stage){
            it?.let { stage = it }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun snackbar(msg: String){
        Snackbar.make(findViewById<View>(R.id.contentFragment), msg, Snackbar.LENGTH_LONG).show()
    }
}