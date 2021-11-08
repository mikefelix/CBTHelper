package com.mozzarelly.cbthelper.analyze

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mozzarelly.cbthelper.CBTFragment
import com.mozzarelly.cbthelper.behavior.BehaviorActivity
import com.mozzarelly.cbthelper.cogvalid.CogValidActivity
import com.mozzarelly.cbthelper.databinding.FragmentAnalyzeSummaryBinding
import com.mozzarelly.cbthelper.editentry.AddEntryActivity
import com.mozzarelly.cbthelper.observe
import com.mozzarelly.cbthelper.replacement.ReplacementThoughtsActivity
import com.mozzarelly.cbthelper.showIf

class AnalyzeSummaryFragment : CBTFragment() {

    val viewModel: AnalyzeViewModel by activityViewModels()

    override val title = "Summary"

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentAnalyzeSummaryBinding.inflate(inflater, container, false).apply {
            observe(viewModel.analysis){ analysis ->
                val stage = analysis?.stage ?: return@observe
                val (entry, cogVal, ratRep, behavior) = analysis

                entryButton.run {
                    text = stage.buttonText("entry", Stage.Begun, Stage.EntryPartial)
                    setOnClickListener {
                        start<AddEntryActivity>(viewModel.id)
                    }
                }

                cogValButton.showIf(stage >= Stage.EntryComplete) {
                    text = stage.buttonText("cognition validity", Stage.EntryComplete, Stage.CogValPartial)
                    setOnClickListener {
                        start<CogValidActivity>(viewModel.id)
                    }
                }

                ratRepButton.showIf(stage >= Stage.CogValComplete) {
                    text = stage.buttonText("replacement thoughts", Stage.CogValComplete, Stage.RatRepPartial)
                    setOnClickListener {
                        start<ReplacementThoughtsActivity>(viewModel.id)
                    }
                }

                behaviorButton.showIf(stage >= Stage.RatRepComplete) {
                    text = stage.buttonText("behavior", Stage.RatRepComplete, Stage.BehaviorPartial)
                    setOnClickListener {
                        start<BehaviorActivity>(viewModel.id)
                    }
                }

                entrySummary.run {
                    text = when (stage) {
                        Stage.Begun -> "Ready to examine."
                        Stage.EntryPartial -> "Interview is in progress..."
                        else -> entry.textSummary()
                    }
                }

                cogValTitle.showIf(stage >= Stage.EntryComplete)
                cogValSummary.showIf(stage >= Stage.EntryComplete){
                    text = when (stage) {
                        Stage.EntryComplete -> "Ready to examine."
                        Stage.CogValPartial -> "Interview is in progress..."
                        else -> cogVal?.textSummary()
                    }
                }

                ratRepTitle.showIf(stage >= Stage.CogValComplete)
                ratRepSummary.showIf(stage >= Stage.CogValComplete){
                    text = when (stage) {
                        Stage.CogValComplete -> "Ready to examine."
                        Stage.RatRepPartial -> "Interview is in progress..."
                        else -> ratRep?.textSummary()
                    }
                }

                behaviorTitle.showIf(stage >= Stage.RatRepComplete)
                behaviorSummary.showIf(stage >= Stage.RatRepComplete){
                    text = when (stage) {
                        Stage.RatRepComplete -> "Ready to examine."
                        Stage.BehaviorPartial -> "Interview is in progress..."
                        else -> behavior?.textSummary()
                    }
                }

                content.visibility = View.VISIBLE
            }
        }.root
}

private fun Stage.buttonText(rest: String, stageReady: Stage, stageInProgress: Stage) = when(this) {
    stageReady -> "Begin"
    stageInProgress -> "Continue"
    else -> "Revisit"
}
