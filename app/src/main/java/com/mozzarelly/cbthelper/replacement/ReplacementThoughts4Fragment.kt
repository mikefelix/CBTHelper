package com.mozzarelly.cbthelper.replacement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.databinding.FragmentReplacement3Binding
import com.mozzarelly.cbthelper.databinding.FragmentReplacement4ImprovedExpressionBinding

class ReplacementThoughts4Fragment : ReplacementThoughtsFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentReplacement4ImprovedExpressionBinding.inflate(inflater, container, false).apply {
            buttons.setupButtons(viewModel.wouldHaveDoneValue)
            wouldHaveDone.bindTo(viewModel.wouldHaveDoneValue)
        }.root
}
