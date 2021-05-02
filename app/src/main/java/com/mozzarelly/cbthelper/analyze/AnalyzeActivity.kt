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
                    Stage.BehaviorComplete -> "Behavior" to AnalyzeBehaviorSummaryFragment()
                    Stage.BehaviorPartial -> "Behavior" to AnalyzeBehaviorSummaryFragment()
                    Stage.RatRepComplete -> "Replacement Thoughts" to AnalyzeRatRepSummaryFragment()
                    Stage.RatRepPartial -> "Replacement Thoughts" to AnalyzeRatRepSummaryFragment()
                    Stage.CogValComplete -> "Cognition Validity" to AnalyzeCogValidSummaryFragment()
                    Stage.CogValPartial -> "Cognition Validity" to AnalyzeCogValidSummaryFragment()
                    Stage.EntryComplete -> "Analyze Entry" to AnalyzeEntrySummaryFragment()
                    Stage.EntryPartial -> "Analyze Entry" to AnalyzeEntrySummaryFragment()
                    Stage.Begun -> "Edit Entry" to AnalyzeEntrySummaryFragment()
                }

                show(frag.second)
                title = frag.first

                field = value
            }
        }

    override val onReturnFrom: Map<KClass<*>, (Int) -> Unit> = mapOf(
        BehaviorActivity::class to {
            snackbar(if (it >= 0) "Behavior analysis saved." else "Behavior analysis could not be saved.")
            viewModel.stage.value?.let { stage = it }
        },
        ReplacementThoughtsActivity::class to {
            snackbar(if (it >= 0) "Replacement thoughts results saved." else "Replacement thoughts results could not be saved.")
            viewModel.stage.value?.let { stage = it }
        },
        CogValidActivity::class to {
            snackbar(if (it >= 0) "Cognition validity results saved." else "Cognition validity results could not be saved.")
            viewModel.stage.value?.let { stage = it }
        }
    )

    override fun AnalyzeViewModel.setup() {
        id = getIdExtra()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = "Analyze"

        observe(viewModel.stage){
            stage = it
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        viewModel.save()
        finish()
        return true
    }

    private fun snackbar(msg: String){
        Snackbar.make(findViewById<View>(R.id.contentFragment), msg, Snackbar.LENGTH_LONG).show()
    }
}