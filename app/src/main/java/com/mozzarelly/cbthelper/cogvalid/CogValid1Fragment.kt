package com.mozzarelly.cbthelper.cogvalid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentCogvalQuestion2RadiosBinding
import com.mozzarelly.cbthelper.databinding.FragmentCogvalQuestion3RadiosBinding

class CogValid1Fragment : CogValidFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentCogvalQuestion3RadiosBinding.inflate(inflater, container, false).apply {
            thoughts.display(viewModel.thoughts)
            bindQuestion(viewModel.answer1Value,
                R.string.validityQuestion1,
                R.string.validityAnswer1A,
                R.string.validityAnswer1B,
                R.string.validityAnswer1C
            )
        }.root

}
