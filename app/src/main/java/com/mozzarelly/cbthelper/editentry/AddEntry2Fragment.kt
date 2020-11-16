package com.mozzarelly.cbthelper.editentry

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.*
import com.mozzarelly.cbthelper.databinding.EmotionSelectionBinding
import com.mozzarelly.cbthelper.databinding.FragmentAdd2ExperienceBinding

class AddEntry2Fragment : AddEntryFragment() {
    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentAdd2ExperienceBinding.inflate(inflater).apply {
            buttons.previous.setOnClickListener {
                previousPage()
            }

            buttons.next.run {
                enableWhenHasValue(viewModel.emotionSelection.emotionsChosen)

                setOnClickListener {
                    nextPage()
                }
            }

            //https://developer.android.com/training/keyboard-input/style#AutoComplete

            emotionSelection.bind(viewModel.emotionSelection)

            observe(viewModel.situationTypeValue) {
                textView1.text = "What emotions did you feel during this ${if (it) "situation" else "conversation"}?"
            }

        }.root

    fun EmotionSelectionBinding.bindQuestion(){

    }
}