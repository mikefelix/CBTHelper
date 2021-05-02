package com.mozzarelly.cbthelper.analyze

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mozzarelly.cbthelper.CBTFragment
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.behavior.BehaviorActivity
import com.mozzarelly.cbthelper.databinding.FragmentAnalyze2CogvalSummaryBinding
import com.mozzarelly.cbthelper.observe
import com.mozzarelly.cbthelper.replacement.ReplacementThoughtsActivity
import java.util.*

class AnalyzeCogValidSummaryFragment : CBTFragment() {

    val viewModel: AnalyzeViewModel by activityViewModels()

    override val title = "Summary"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentAnalyze2CogvalSummaryBinding.inflate(inflater, container, false).apply {
            observe(viewModel.thinkingErrors) {
                errors.text = it.joinToString(separator = "\n")
            }

            observe(viewModel.emotionsChosen) {
                text3.text = getString(R.string.validitySummary3, it?.toLowerCase(Locale.US))
            }

            observe(viewModel.ratRep){
                when {
                    it == null -> {
                        continueRationalButton.visibility = View.GONE
                        beginRationalButton.run {
                            setText(R.string.begin_replacement_thoughts)
                            setOnClickListener {
                                start<ReplacementThoughtsActivity>(viewModel.id)
                            }
                        }
                    }
                    it.complete -> {
                        continueRationalButton.visibility = View.GONE
                        beginRationalButton.run {
                            setText(R.string.view_replacement_thoughts)
                            setOnClickListener {
                                act.showFragment<AnalyzeRatRepSummaryFragment>()
                            }
                        }
                    }
                    else -> {
                        continueRationalButton.visibility = View.VISIBLE
                        beginRationalButton.run {
                            setText(R.string.continue_replacement_thoughts)
                            setOnClickListener {
                                start<ReplacementThoughtsActivity>(viewModel.id)
                            }
                        }
                    }
                }
            }

            skipRationalButton.setOnClickListener {
                start<BehaviorActivity>(viewModel.id)
            }
        }.root

}
