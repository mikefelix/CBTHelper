package com.mozzarelly.cbthelper.behavior

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentBehaviorIntroBinding
import com.mozzarelly.cbthelper.databinding.FragmentBehaviorQuestion3RadiosBinding
import com.mozzarelly.cbthelper.databinding.FragmentBehaviorQuestionTextBinding

class Behavior1AFragment : BehaviorFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentBehaviorQuestionTextBinding.inflate(inflater, container, false).apply {
            buttons.setupButtons(enableNextWhenFilled = viewModel.personValue)
            expression.display(viewModel.expression)

            question.setText(R.string.behavior1Question1)

            answer.bindTo(viewModel.personValue)
        }.root
}