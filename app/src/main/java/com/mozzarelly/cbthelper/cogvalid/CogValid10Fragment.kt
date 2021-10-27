package com.mozzarelly.cbthelper.cogvalid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentCogvalQuestion2RadiosBinding

class CogValid10Fragment : CogValidFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentCogvalQuestion2RadiosBinding.inflate(inflater, container, false).apply {
            thoughts.display(viewModel.thoughts)
            question.display(R.string.validityQuestion10, viewModel.answer8 ?: "he/she")
            radioAnswer1.bindTo(viewModel.answer10Value,  R.string.validityAnswer10A, 1)
            radioAnswer2.bindTo(viewModel.answer10Value,  R.string.validityAnswer10B, 2)

            buttons.previous.setOnClickListener { previousPage() }

            buttons.next.run {
                enableWhenHasValue(viewModel.answer10Value)
                setOnClickListener {
                    viewModel.save()
                    act.finish(viewModel.answer10Value.value ?: 1) // 1 == rational, 0 == irrational
                }
            }
        }.root

}
