package com.mozzarelly.cbthelper.cogvalid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentCogvalQuestion3RadiosBinding
import com.mozzarelly.cbthelper.databinding.FragmentCogvalQuestion4NestedradiosBinding
import com.mozzarelly.cbthelper.databinding.FragmentCogvalQuestion5NestedradiosBinding

class CogValid8Fragment : CogValidFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentCogvalQuestion4NestedradiosBinding.inflate(inflater, container, false).apply {
            thoughts.text = viewModel.thoughts
            bindQuestion(viewModel.answer8Value,
                R.string.validityQuestion8,
                Pair(R.string.validityAnswer8A, R.string.validityAnswer8B),
                listOf(R.string.validityAnswer8A1, R.string.validityAnswer8A2, R.string.validityAnswer8A3, R.string.validityAnswer8A4)
            )
        }.root

}
