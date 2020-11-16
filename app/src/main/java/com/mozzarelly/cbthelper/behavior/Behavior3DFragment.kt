package com.mozzarelly.cbthelper.behavior

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentBehaviorIntroBinding
import com.mozzarelly.cbthelper.databinding.FragmentBehaviorQuestion3Radios2QuestionsBinding
import com.mozzarelly.cbthelper.databinding.FragmentBehaviorQuestion3RadiosBinding

class Behavior3DFragment : BehaviorFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentBehaviorQuestion3Radios2QuestionsBinding.inflate(inflater, container, false).apply {
            buttons.setupButtons(enableNextWhenFilled = viewModel.otherValue)
            expression.displayDatum(viewModel.expression)

            question1.setText(R.string.behavior3Question)
            question2.setText(R.string.behavior3Question4)

            radioAnswer1.bindTo(viewModel.otherValue, R.string.behavior3Answer1, 1)
            radioAnswer2.bindTo(viewModel.otherValue, R.string.behavior3Answer2, 2)
            radioAnswer3.bindTo(viewModel.otherValue, R.string.behavior3Answer3, 3)

        }.root
}