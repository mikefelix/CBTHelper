package com.mozzarelly.cbthelper.behavior

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentBehaviorResultsBinding

class BehaviorResultsFragment : BehaviorFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentBehaviorResultsBinding.inflate(inflater, container, false).apply {
            buttons.previous.setOnClickListener { previousPage() }

            if (viewModel.skipTest || viewModel.isRational) {
                buttons.next.run {
                    enableWhenHasValue(viewModel.otherPersonRationalValue)
                    text = getString(R.string.summary)

                    setOnClickListener {
                        // Fill out the unused answers so it will be complete.
                        viewModel.moreRationalActions = "N/A"
                        viewModel.moreRationalAffect = "N/A"
                        viewModel.otherBehaviorRational = 1

                        viewModel.save()
                        act.finish(1)
                    }
                }

                text1.display(R.string.behaviorResultsRat1, viewModel.situationType.value ?: "situation")
                text2.display(R.string.behaviorResultsRat2)
                text3.display(R.string.behaviorResultsRat3)
                noErrors.visibility = View.VISIBLE
                errors.visibility = View.GONE
                reacted.display(viewModel.reacted)
                otherBehavior.visibility = View.VISIBLE
                radioAnswer1.bindTo(viewModel.otherPersonRationalValue, R.string.rational, 1)
                radioAnswer2.bindTo(viewModel.otherPersonRationalValue, R.string.irrational, 2)
            }
            else {
                buttons.next.run {
                    text = getString(R.string.next)
                    setOnClickListener {
                        viewModel.save()
                        nextPage()
                    }
                }

                noErrors.visibility = View.GONE
                errors.display(viewModel.errors){ it?.joinToString("\n") ?: "" }
                text1.display(R.string.behaviorResultsIrrat1)
                text2.display(R.string.behaviorResultsIrrat2)
                text3.visibility = View.GONE
                reacted.visibility = View.GONE
                otherBehavior.visibility = View.GONE
            }
        }.root
}
