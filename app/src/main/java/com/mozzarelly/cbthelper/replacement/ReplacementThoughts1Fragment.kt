package com.mozzarelly.cbthelper.replacement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mozzarelly.cbthelper.databinding.FragmentReplacement1Binding
import com.mozzarelly.cbthelper.emotionhelp.EmotionHelpActivity

class ReplacementThoughts1Fragment : ReplacementThoughtsFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentReplacement1Binding.inflate(inflater, container, false).apply {
            buttons.setupButtons(viewModel.thinkInsteadValue)
            thoughts.display(viewModel.thoughts)
            situation.display(viewModel.situation)
            errors.display(viewModel.thinkingErrors)
            answer.display(viewModel.thinkInsteadValue)
            helpButton.setOnClickListener {
                start<EmotionHelpActivity>(viewModel.id)
            }
        }.root
}