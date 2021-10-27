package com.mozzarelly.cbthelper.cogvalid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentCogvalQuestion3CheckboxesBinding
import com.mozzarelly.cbthelper.databinding.FragmentCogvalQuestion3RadiosBinding

class CogValid5Fragment : CogValidFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentCogvalQuestion3CheckboxesBinding.inflate(inflater, container, false).apply {
            thoughts.display(viewModel.thoughts)
            bindQuestion(viewModel.answer5Value,
                R.string.validityQuestion5,
                R.string.validityAnswer5A,
                R.string.validityAnswer5B,
                R.string.validityAnswer5C
            )
        }.root

}
