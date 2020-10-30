package com.mozzarelly.cbthelper.cogvalid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentCogvalQuestion2RadiosBinding

class CogValid10Fragment : CogValidFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentCogvalQuestion2RadiosBinding.inflate(inflater, container, false).apply {
            thoughts.display(viewModel.thoughtsValue)
            bindQuestion(viewModel.answer10Value,
                R.string.validityQuestion10,
                R.string.validityAnswer10A,
                R.string.validityAnswer10B
            )
        }.root

}
