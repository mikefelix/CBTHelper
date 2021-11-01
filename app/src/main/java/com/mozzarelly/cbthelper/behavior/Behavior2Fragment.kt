package com.mozzarelly.cbthelper.behavior

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentBehaviorQuestion3RadiosBinding

class Behavior2Fragment : BehaviorFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentBehaviorQuestion3RadiosBinding.inflate(inflater, container, false).apply {
            buttons.setupButtons(enableNextWhenFilled = viewModel.honestValue)
            expression.display(viewModel.expression)

            question.setText(R.string.behavior2Question)

            radioAnswer1.bindTo(viewModel.honestValue, R.string.behavior2Answer1, 1)
            radioAnswer2.bindTo(viewModel.honestValue, R.string.behavior2Answer2, 2)
            radioAnswer3.bindTo(viewModel.honestValue, R.string.behavior2Answer3, 3)

        }.root
}