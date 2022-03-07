package com.mozzarelly.cbthelper.analyze

import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.mozzarelly.cbthelper.*
import com.mozzarelly.cbthelper.behavior.BehaviorActivity
import com.mozzarelly.cbthelper.cogvalid.CogValidActivity
import com.mozzarelly.cbthelper.databinding.FragmentAnalyzeSummaryBinding
import com.mozzarelly.cbthelper.editentry.AddEntryActivity
import com.mozzarelly.cbthelper.replacement.ReplacementThoughtsActivity
import kotlin.reflect.KClass

class AnalyzeActivity : CBTActivity<AnalyzeViewModel>() {

    override val layout: Int = R.layout.activity_analyze

    override val viewModel: AnalyzeViewModel by cbtViewModel()

    override val onReturnFrom: Map<KClass<*>, (SaveResult) -> Unit> = mapOf(
        BehaviorActivity::class to {
            snackbar(if (it.saved) "Behavior analysis saved." else "Behavior analysis could not be saved.")
//            viewModel.stage.value?.let { stage = it }
        },
        ReplacementThoughtsActivity::class to {
            snackbar(if (it.saved) "Replacement thoughts results saved." else "Replacement thoughts results could not be saved.")
//            viewModel.stage.value?.let { stage = it }
        },
        CogValidActivity::class to {
            snackbar(if (it.saved) "Cognition validity results saved." else "Cognition validity results could not be saved.")
//            viewModel.stage.value?.let { stage = it }
        }
    )

    override fun AnalyzeViewModel.setup() {
        id = getIdExtra()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = "Analyze"
        viewModel.id = getIdExtra()

        show(AnalyzeSummaryFragment())
/*
        observe(viewModel.stage){
            when (it) {
                Stage.BehaviorComplete -> {
//                        title = "Behavior"
                    show(AnalyzeBehaviorSummaryFragment())
                }
                Stage.BehaviorPartial -> {
                    start<BehaviorActivity>(viewModel.id)
                }
                Stage.RatRepComplete -> {
//                        title = "Replacement Thoughts"
                    show(AnalyzeRatRepSummaryFragment())
                }
                Stage.RatRepPartial -> {
                    start<ReplacementThoughtsActivity>(viewModel.id)
                }
                Stage.CogValComplete -> {
//                        title = "Cognition Validity"
                    show(AnalyzeCogValidSummaryFragment())
                }
                Stage.CogValPartial -> {
                    start<CogValidActivity>(viewModel.id)
                }
                Stage.EntryComplete -> {
//                        title = "Analyze Entry"
                    show(AnalyzeEntrySummaryFragment())
                }
                Stage.EntryPartial -> {
                    start<AddEntryActivity>(viewModel.id)
                }
                Stage.Begun -> {
                    start<AddEntryActivity>(viewModel.id)
                }
            }
        }
*/
    }

    override fun onSupportNavigateUp(): Boolean {
//        viewModel.save()
        finish()
        return true
    }

    private fun snackbar(msg: String){
        Snackbar.make(findViewById<View>(R.id.contentFragment), msg, Snackbar.LENGTH_LONG).show()
    }
}