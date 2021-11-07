package com.mozzarelly.cbthelper.analyze

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mozzarelly.cbthelper.CBTFragment
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.cogvalid.CogValidActivity
import com.mozzarelly.cbthelper.databinding.FragmentAnalyze1EntrySummaryBinding
import com.mozzarelly.cbthelper.makeBulletedList
import com.mozzarelly.cbthelper.observe

class AnalyzeEntrySummaryFragment : CBTFragment() {

    val viewModel: AnalyzeViewModel by activityViewModels()// { act.viewModelProvider }

    override val title = "Summary"
//        get() = if (viewModel.entry.value?.situationType == false) "Conversation Summary" else "Situation Summary"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        FragmentAnalyze1EntrySummaryBinding.inflate(inflater, container, false).apply {
            pageTitle.display(viewModel.typeStringCapitalized)
            situationLabel.display(R.string.validitySituationLabel, viewModel.typeString.value ?: "situation")
            situation.display(viewModel.situation)
            emotions.display(viewModel.emotionsFelt)
            testInvitation.display(R.string.validityTestInvitation)

            observe(viewModel.cogValid){
                when {
                    it == null -> testButton.run {
                            setText(R.string.begin_cognition_validity_test)
                            setOnClickListener {
                                start<CogValidActivity>(viewModel.id)
                            }
                        }
                    it.complete -> testButton.run {
                            setText(R.string.view_cognition_validity_test)
                            setOnClickListener {
                                act.showFragment<AnalyzeCogValidSummaryFragment>()
                            }
                        }
                    else -> testButton.run {
                            setText(R.string.continue_cognition_validity_test)
                            setOnClickListener {
                                start<CogValidActivity>(viewModel.id)
                            }
                        }
                }
            }

        }.root

}