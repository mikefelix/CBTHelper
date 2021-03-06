package com.mozzarelly.cbthelper.replacement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.databinding.FragmentReplacement1Binding

class ReplacementThoughts1Fragment : ReplacementThoughtsFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentReplacement1Binding.inflate(inflater, container, false).apply {
            buttons.setupButtons(viewModel.insteadValue)
            thoughts.display(viewModel.thoughts)
            situation.display(viewModel.situation)
            errors.display(viewModel.thinkingErrors)
            answer.bindTo(viewModel.insteadValue)
        }.root
}