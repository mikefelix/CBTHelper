package com.mozzarelly.cbthelper.behavior

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentBehaviorQuestion3RadiosBinding

class Behavior1DFragment : BehaviorFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentBehaviorQuestion3RadiosBinding.inflate(inflater, container, false).apply {
            bindPersonQuestion(viewModel.embarrassedValue, R.string.behavior1Question4)
        }.root

}