package com.mozzarelly.cbthelper.cogvalid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentCogvalQuestion3RadiosBinding

class CogValid2Fragment : CogValidFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentCogvalQuestion3RadiosBinding.inflate(inflater, container, false).apply {
            thoughts.display(viewModel.thoughtsValue)
            bindQuestion(viewModel.answer2Value,
                R.string.validityQuestion2,
                R.string.validityAnswer2A,
                R.string.validityAnswer2B,
                R.string.validityAnswer2C
            )
        }.root

}
