package com.mozzarelly.cbthelper.behavior

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentBehaviorNewReactions1Binding

class BehaviorNewReactions1Fragment : BehaviorFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentBehaviorNewReactions1Binding.inflate(inflater, container, false).apply {
            buttons.setupButtons(viewModel.moreRationalAffectValue)
            moreRational.display(viewModel.moreRationalActionsValue)
            text1.display(R.string.behaviorResultsReactions1)
            answer.bindTo(viewModel.moreRationalAffectValue)
        }.root
}
