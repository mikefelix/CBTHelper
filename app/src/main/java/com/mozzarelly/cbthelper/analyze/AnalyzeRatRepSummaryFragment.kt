package com.mozzarelly.cbthelper.analyze

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mozzarelly.cbthelper.CBTFragment
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.behavior.BehaviorActivity
import com.mozzarelly.cbthelper.databinding.FragmentAnalyze3RatrepSummaryBinding
import com.mozzarelly.cbthelper.observe

class AnalyzeRatRepSummaryFragment : CBTFragment() {

    val viewModel: AnalyzeViewModel by activityViewModels()

    override val title = "Summary"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentAnalyze3RatrepSummaryBinding.inflate(inflater, container, false).apply {
            observe(viewModel.behavior){
                when {
                    it == null -> {
                        continueButton.visibility = View.GONE
                        testButton.run {
                            setText(R.string.begin_behavior_validity_test)
                            setOnClickListener {
                                start<BehaviorActivity>(viewModel.id)
                            }
                        }
                    }
                    it.complete -> {
                        continueButton.visibility = View.GONE
                        testButton.run {
                            setText(R.string.view_behavior_validity_test)
                            setOnClickListener {
                                act.showFragment<AnalyzeBehaviorSummaryFragment>()
                            }
                        }
                    }
                    else -> {
                        continueButton.run {
                            visibility = View.VISIBLE
                            setOnClickListener {
                                start<BehaviorActivity>(viewModel.id)
                            }
                        }
                        testButton.run {
                            setText(R.string.continue_behavior_validity_test)
                            setOnClickListener {
                                start<BehaviorActivity>(viewModel.id)
                            }
                        }
                    }
                }
            }

            thought.display(viewModel.thoughts)
            instead.display(viewModel.instead)
            emotions.display(viewModel.actualEmotions)
            wouldHaveFelt.display(viewModel.possibleEmotions)
            expressed.display(viewModel.expressed)
            reacted.display(viewModel.relationships)
            comparison.display(viewModel.comparison)
        }.root
}
