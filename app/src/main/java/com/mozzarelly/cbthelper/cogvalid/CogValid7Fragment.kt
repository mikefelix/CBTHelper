package com.mozzarelly.cbthelper.cogvalid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentCogvalQuestion6CheckboxesBinding
import com.mozzarelly.cbthelper.databinding.FragmentCogvalQuestion6RadiosBinding

class CogValid7Fragment : CogValidFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentCogvalQuestion6CheckboxesBinding.inflate(inflater, container, false).apply {
            thoughts.display(viewModel.thoughts)
            bindQuestion(viewModel.answer7Value,
                R.string.validityQuestion7,
                R.string.validityAnswer7A,
                R.string.validityAnswer7B,
                R.string.validityAnswer7C,
                R.string.validityAnswer7D,
                R.string.validityAnswer7E,
                R.string.validityAnswer7F
            )
        }.root

}
