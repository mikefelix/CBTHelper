package com.mozzarelly.cbthelper.analyze

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mozzarelly.cbthelper.CBTFragment
import com.mozzarelly.cbthelper.behavior.BehaviorActivity
import com.mozzarelly.cbthelper.databinding.FragmentAnalyze3RatrepSummaryBinding

class AnalyzeRatRepSummaryFragment : CBTFragment() {

    val viewModel: AnalyzeViewModel by activityViewModels()

    override val title = "Summary"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentAnalyze3RatrepSummaryBinding.inflate(inflater, container, false).apply {
            testButton.setOnClickListener {
                start<BehaviorActivity>(viewModel.id)
            }

            thought.displayDatum(viewModel.thoughts)
            instead.displayDatum(viewModel.instead)
            emotions.displayDatum(viewModel.actualEmotions)
            wouldHaveFelt.displayDatum(viewModel.possibleEmotions)
            expressed.displayDatum(viewModel.expressed)
            reacted.displayDatum(viewModel.relationships)

            comparison.bindTo(viewModel.comparison)
            comparison.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus){
                    viewModel.save()
                }
            }
        }.root
}
