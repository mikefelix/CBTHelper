package com.mozzarelly.cbthelper.replacement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentReplacement3Binding
import com.mozzarelly.cbthelper.databinding.FragmentReplacement4ImprovedExpressionBinding
import com.mozzarelly.cbthelper.databinding.FragmentReplacement5ImprovedExpressionBinding

class ReplacementThoughts5Fragment : ReplacementThoughtsFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentReplacement5ImprovedExpressionBinding.inflate(inflater, container, false).apply {
            buttons.previous.setOnClickListener { previousPage() }

            buttons.next.run {
                text = getString(R.string.finish)

                setOnClickListener {
//                    viewModel.complete = true
                    viewModel.save()
                    act.finish(1)
                }
            }

            wouldHaveDone.display(viewModel.wouldHaveDoneValue)
            answer.bindTo(viewModel.wouldHaveAffectedValue)
        }.root
}
