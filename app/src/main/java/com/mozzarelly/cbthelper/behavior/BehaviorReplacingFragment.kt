package com.mozzarelly.cbthelper.behavior

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.data
import com.mozzarelly.cbthelper.databinding.FragmentBehaviorReplaceBinding

class BehaviorReplacingFragment : BehaviorFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentBehaviorReplaceBinding.inflate(inflater, container, false).apply {
            buttons.setupButtons(viewModel.moreRationalActionsValue)

            if (viewModel.cogValRational.value == true) {
                text1.display(R.string.behaviorResultsRepRatCog1, viewModel.situationType.data(), viewModel.feltEmotionsSimple.data())
                text2.visibility = View.GONE
                replaced.visibility = View.GONE
                answer.bindTo(viewModel.moreRationalActionsValue)
            }
            else {
                text1.display(R.string.behaviorResultsRepIrratCog1, viewModel.situationType.data(), viewModel.insteadEmotions.data())
                text2.display(R.string.behaviorResultsRepIrratCog2, viewModel.feltEmotionsSimple.data())
                replaced.display(viewModel.replacedThoughts)
                answer.bindTo(viewModel.moreRationalActionsValue)
            }
        }.root
}
