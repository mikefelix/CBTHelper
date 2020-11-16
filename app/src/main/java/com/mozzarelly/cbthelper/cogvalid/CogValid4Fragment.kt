package com.mozzarelly.cbthelper.cogvalid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentCogvalQuestion2RadiosBinding
import com.mozzarelly.cbthelper.databinding.FragmentCogvalQuestion2TextsBinding

class CogValid4Fragment : CogValidFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentCogvalQuestion2TextsBinding.inflate(inflater, container, false).apply {
            thoughts.display(viewModel.thoughts)
            bindQuestion(R.string.validityQuestion4A, viewModel.answer4aValue, R.string.validityQuestion4B, viewModel.answer4bValue)
        }.root

}
