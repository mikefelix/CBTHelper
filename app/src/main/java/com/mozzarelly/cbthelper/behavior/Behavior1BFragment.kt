package com.mozzarelly.cbthelper.behavior

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentBehaviorQuestion3RadiosBinding

class Behavior1BFragment : BehaviorFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentBehaviorQuestion3RadiosBinding.inflate(inflater, container, false).apply {
            bindPersonQuestion(viewModel.disappointedValue, R.string.behavior1Question2)
        }.root
}