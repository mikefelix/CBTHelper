package com.mozzarelly.cbthelper.cogvalid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentCogvalQuestion2RadiosBinding
import com.mozzarelly.cbthelper.databinding.FragmentCogvalQuestion3RadiosBinding
import com.mozzarelly.cbthelper.databinding.FragmentCogvalQuestion5NestedradiosBinding

class CogValid9Fragment : CogValidFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentCogvalQuestion2RadiosBinding.inflate(inflater, container, false).apply {
            thoughts.display(viewModel.thoughts)
            bindQuestion(viewModel.answer9Value,
                getString(R.string.validityQuestion9, viewModel.answer8 ?: "he/she"),
                R.string.validityAnswer9A,
                R.string.validityAnswer9B
            )
        }.root

}
