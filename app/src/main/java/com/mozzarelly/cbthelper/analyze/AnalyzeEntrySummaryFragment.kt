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
            pageTitle.display(R.string.apples_metaphor)
            metaphor1.display(R.string.apples_metaphor_1)
            metaphor2.text = getString(R.string.apples_metaphor_2)
            metaphorBullets.text = makeBulletedList(8, requireContext(), R.string.apples_metaphor_bullets_1, R.string.apples_metaphor_bullets_2, R.string.apples_metaphor_bullets_3)
            metaphor3.display(R.string.apples_metaphor_3)
            testInvitation.display(R.string.check_for_apples)
//            situation.displayDatum(viewModel.situation)
//            emotions.displayDatum(viewModel.emotionsChosen)

            observe(viewModel.cogValid){
                when {
                    it == null -> {
                        continueButton.visibility = View.GONE
                        testButton.run {
                            setText(R.string.begin_cognition_validity_test)
                            setOnClickListener {
                                start<CogValidActivity>(viewModel.id)
                            }
                        }
                    }
                    it.complete -> {
                        continueButton.visibility = View.GONE
                        testButton.run {
                            setText(R.string.view_cognition_validity_test)
                            setOnClickListener {
                                act.showFragment<AnalyzeCogValidSummaryFragment>()
                            }
                        }
                    }
                    else -> {
                        continueButton.visibility = View.VISIBLE
                        continueButton.run {
                            setText(R.string.continue_cognition_validity_test)
                            setOnClickListener {
                                start<CogValidActivity>(viewModel.id)
                            }
                        }
                        testButton.run {
                            setText(R.string.continue_cognition_validity_test)
                            setOnClickListener {
                                start<CogValidActivity>(viewModel.id)
                            }
                        }
                    }
                }
            }

        }.root

}