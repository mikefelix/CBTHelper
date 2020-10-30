package com.mozzarelly.cbthelper.cogvalid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentCogvalQuestion3RadiosBinding
import com.mozzarelly.cbthelper.databinding.FragmentCogvalQuestion5NestedradiosBinding

class CogValid9Fragment : CogValidFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentCogvalQuestion5NestedradiosBinding.inflate(inflater, container, false).apply {
            thoughts.display(viewModel.thoughtsValue)
            bindQuestion(viewModel.answer9Value,
                R.string.validityQuestion9,
                Pair(R.string.validityAnswer9A, R.string.validityAnswer9B),
                listOf(R.string.validityAnswer9A1, R.string.validityAnswer9A2, R.string.validityAnswer9A3, R.string.validityAnswer9A4, R.string.validityAnswer9A5)
            )
        }.root

}
