package com.mozzarelly.cbthelper.cogvalid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentCogvalQuestion5CheckboxesBinding
import com.mozzarelly.cbthelper.databinding.FragmentCogvalQuestion5RadiosBinding

class CogValid6Fragment : CogValidFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentCogvalQuestion5CheckboxesBinding.inflate(inflater, container, false).apply {
            thoughts.display(viewModel.thoughts)
            bindQuestion(viewModel.answer6Value,
                R.string.validityQuestion6,
                R.string.validityAnswer6A,
                R.string.validityAnswer6B,
                R.string.validityAnswer6C,
                R.string.validityAnswer6D,
                R.string.validityAnswer6E
            )
        }.root

}
