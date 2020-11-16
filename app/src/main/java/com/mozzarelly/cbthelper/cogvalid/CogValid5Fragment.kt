package com.mozzarelly.cbthelper.cogvalid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentCogvalQuestion3RadiosBinding
import com.mozzarelly.cbthelper.databinding.FragmentCogvalQuestion5NestedradiosBinding

class CogValid5Fragment : CogValidFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentCogvalQuestion5NestedradiosBinding.inflate(inflater, container, false).apply {
            thoughts.display(viewModel.thoughts)
            bindQuestion(viewModel.answer5Value,
                R.string.validityQuestion5,
                Pair(R.string.validityAnswer5A, R.string.validityAnswer5B),
                listOf(R.string.validityAnswer5A1, R.string.validityAnswer5A2, R.string.validityAnswer5A3, R.string.validityAnswer5A4, R.string.validityAnswer5A5)
            )
        }.root

}
