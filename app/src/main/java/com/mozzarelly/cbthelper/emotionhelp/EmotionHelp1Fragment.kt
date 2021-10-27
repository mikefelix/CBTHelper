package com.mozzarelly.cbthelper.emotionhelp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentEmhelp1Binding

class EmotionHelp1Fragment : EmotionHelpFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentEmhelp1Binding.inflate(inflater, container, false).apply {
            buttons.next.run {
//                isEnabled = !viewModel.desiredEmotion.value?.toString().isNullOrBlank()
                enableWhenHasValue(viewModel.desiredEmotion)
                setOnClickListener { nextPage() }
            }

            buttons.previous.run {
                visibility = View.INVISIBLE
                setOnClickListener { error("Can't go that way.") }
            }

            situation.display(viewModel.situation)
            emotionSelection.bind(viewModel.desiredEmotion)

            readMore.setOnClickListener {
                showExplanationPopup(R.string.negative_emotion_heading, R.string.negative_emotion_explanation)
            }

        }.root
}