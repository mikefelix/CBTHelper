package com.mozzarelly.cbthelper.replacement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentReplacement3Binding
import com.mozzarelly.cbthelper.databinding.FragmentReplacement4ImprovedExpressionBinding
import com.mozzarelly.cbthelper.databinding.FragmentReplacement4LetsCompareBinding
import com.mozzarelly.cbthelper.emotionhelp.EmotionHelpActivity

class ReplacementThoughts4Fragment : ReplacementThoughtsFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentReplacement4LetsCompareBinding.inflate(inflater, container, false).apply {
            thoughts.display(viewModel.thoughts)
            emotions.display(viewModel.actualEmotionText)
            insteadThought.display(viewModel.insteadValue)
            insteadFelt.display(viewModel.insteadFelt)

            buttons.yes.setOnClickListener {
                viewModel.comparison = "Yes"
                nextPage()
            }

            buttons.no.setOnClickListener {
                viewModel.comparison = "No"
                viewModel.save()
                act.finish(1)
            }

            buttons.dontKnow.setOnClickListener {
                viewModel.comparison = "I don't know"
                viewModel.save()
                act.finish(1)
            }
        }.root
}
