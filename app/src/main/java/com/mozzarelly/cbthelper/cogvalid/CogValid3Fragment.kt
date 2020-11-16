package com.mozzarelly.cbthelper.cogvalid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentCogvalQuestion3RadiosBinding

class CogValid3Fragment : CogValidFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentCogvalQuestion3RadiosBinding.inflate(inflater, container, false).apply {
            thoughts.display(viewModel.thoughts)
            bindQuestion(viewModel.answer3Value,
                R.string.validityQuestion3,
                R.string.validityAnswer3A,
                R.string.validityAnswer3B,
                R.string.validityAnswer3C
            )
        }.root

}
