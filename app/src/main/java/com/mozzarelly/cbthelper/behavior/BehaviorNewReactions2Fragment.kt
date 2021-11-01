package com.mozzarelly.cbthelper.behavior

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentBehaviorNewReactions2Binding

class BehaviorNewReactions2Fragment : BehaviorFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentBehaviorNewReactions2Binding.inflate(inflater, container, false).apply {
            buttons.previous.setOnClickListener { previousPage() }
            buttons.next.run {
                enableWhenHasValue(viewModel.otherBehaviorRationalValue)
                text = getString(R.string.summary)

                setOnClickListener {
                    viewModel.save()
                    act.finish(1)
                }
            }

            moreRationalAffect.display(viewModel.moreRationalAffectValue)
            text1.display(R.string.behaviorResultsReactions2)

            radioAnswer1.bindTo(viewModel.otherBehaviorRationalValue, R.string.rational, 1)
            radioAnswer2.bindTo(viewModel.otherBehaviorRationalValue, R.string.irrational, 2)
        }.root
}
