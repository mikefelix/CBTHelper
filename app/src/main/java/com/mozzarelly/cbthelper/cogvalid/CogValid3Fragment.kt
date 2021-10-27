package com.mozzarelly.cbthelper.cogvalid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentCogvalQuestion7CheckboxesBinding

class CogValid3Fragment : CogValidFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentCogvalQuestion7CheckboxesBinding.inflate(inflater, container, false).apply {
            thoughts.display(viewModel.thoughts)
            bindQuestion(viewModel.answer3Value,
                R.string.validityQuestion3,
                R.string.validityAnswer3A,
                R.string.validityAnswer3B,
                R.string.validityAnswer3C,
                R.string.validityAnswer3D,
                R.string.validityAnswer3E,
                R.string.validityAnswer3F,
                R.string.validityAnswer3G
            )
        }.root

}
