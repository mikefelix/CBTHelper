package com.mozzarelly.cbthelper.cogvalid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentCogvalQuestion2RadiosBinding
import com.mozzarelly.cbthelper.databinding.FragmentCogvalQuestion2TextsBinding
import com.mozzarelly.cbthelper.databinding.FragmentCogvalQuestionTextBinding

class CogValid11Fragment : CogValidFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentCogvalQuestionTextBinding.inflate(inflater, container, false).apply {
            thoughts.display(viewModel.thoughtsValue)
            bindQuestion(R.string.validityQuestion11, viewModel.answer11Value)
        }.root

}
