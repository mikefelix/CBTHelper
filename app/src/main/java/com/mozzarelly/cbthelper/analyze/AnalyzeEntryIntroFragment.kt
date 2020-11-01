package com.mozzarelly.cbthelper.analyze

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mozzarelly.cbthelper.CBTFragment
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.cogvalid.CogValidActivity
import com.mozzarelly.cbthelper.databinding.FragmentAnalyzeIntroBinding
import com.mozzarelly.cbthelper.observe

class AnalyzeEntryIntroFragment : CBTFragment() {

    val viewModel: AnalyzeEntryViewModel by activityViewModels()// { act.viewModelProvider }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentAnalyzeIntroBinding.inflate(inflater, container, false).apply {
            situation.display(viewModel.situation)
            emotions.display(viewModel.emotionsChosen)

            observe(viewModel.cogValid){
                when {
                    it == null -> {
                        testButton.setText(R.string.begin_cognition_validity_test)
                        testButton.setOnClickListener {
                            start<CogValidActivity>(RequestCodeStartCogValidTest,"id" to viewModel.id.toString())
                        }
                    }
                    it.complete -> {
                        testButton.setText(R.string.view_cognition_validity_test)
                        testButton.setOnClickListener {
                            act.showFragment<CogValidSummaryFragment>()
                        }
                    }
                    else -> {
                        testButton.setText(R.string.continue_cognition_validity_test)
                        testButton.setOnClickListener {
                            start<CogValidActivity>(RequestCodeStartCogValidTest,"id" to viewModel.id.toString())
                        }
                    }
                }
            }

        }.root

}