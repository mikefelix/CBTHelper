package com.mozzarelly.cbthelper.editentry

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.*
import com.mozzarelly.cbthelper.databinding.FragmentAdd2ExperienceBinding

class AddEntry2Fragment : AddEntryFragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentAdd2ExperienceBinding.inflate(inflater).apply {
            buttons.previous.setOnClickListener {
                previousPage()
            }

            buttons.next.run {
                enableWhenHasValue(viewModel.emotion1Value)

                setOnClickListener {
                    nextPage()
                }
            }

            //https://developer.android.com/training/keyboard-input/style#AutoComplete

            listOf(emotion1, emotion2, emotion3).bind(viewModel.emotion1Value, viewModel.emotion2Value, viewModel.emotion3Value)

            observe(viewModel.situationTypeValue) {
                textView1.text = "What emotions did you feel during this ${if (it) "situation" else "conversation"}?"
            }

        }.root

}