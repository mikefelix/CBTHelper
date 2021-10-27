package com.mozzarelly.cbthelper.replacement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentReplacement2Binding
import com.mozzarelly.cbthelper.emotionhelp.EmotionHelpActivity

class ReplacementThoughts2Fragment : ReplacementThoughtsFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentReplacement2Binding.inflate(inflater, container, false).apply {
            slider.bind(viewModel.believeValue, R.string.replacement2Question, R.string.completely_false, R.string.completely_true)

            thoughts.display(viewModel.thoughts)

            buttons.previous.setOnClickListener { previousPage() }

            buttons.next.run {
                enableWhenHasValue(viewModel.believeValue)
                setOnClickListener {
                    println("It's ${viewModel.believeValue.value}")
                    if (viewModel.believeValue.value!! >= 6)
                        nextPage()
                    else
                        start<EmotionHelpActivity>(viewModel.id)
                }
            }

        }.root
}
