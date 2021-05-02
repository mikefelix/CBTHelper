package com.mozzarelly.cbthelper.replacement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.databinding.FragmentReplacement3Binding

class ReplacementThoughts3Fragment : ReplacementThoughtsFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentReplacement3Binding.inflate(inflater, container, false).apply {
            buttons.setupButtons(viewModel.emotionSelection.emotion1)
            emotionSel.bind(viewModel.emotionSelection)
            instead.display(viewModel.insteadValue)
        }.root
}
