package com.mozzarelly.cbthelper.replacement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentReplacement3Binding

class ReplacementThoughts3Fragment : ReplacementThoughtsFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentReplacement3Binding.inflate(inflater, container, false).apply {
            buttons.setupButtons(viewModel.emotion1Value)

            listOf(emotion1, emotion2, emotion3).bind(viewModel.emotion1Value, viewModel.emotion2Value, viewModel.emotion3Value)

            insteadHeading.display(R.string.replacement3Question1)
            emotionsHeading.display(R.string.replacement3Question2)

            instead.display(viewModel.thinkInsteadValue)
        }.root
}
