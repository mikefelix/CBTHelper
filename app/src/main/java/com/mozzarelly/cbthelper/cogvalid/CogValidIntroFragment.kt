package com.mozzarelly.cbthelper.cogvalid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.databinding.FragmentCogvalIntroBinding

class CogValidIntroFragment : CogValidFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentCogvalIntroBinding.inflate(inflater, container, false).apply {
            buttons.setupButtons()
            buttons.previous.visibility = View.INVISIBLE
            thoughts.display(viewModel.thoughts)
        }.root

}
