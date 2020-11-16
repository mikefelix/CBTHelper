package com.mozzarelly.cbthelper.behavior

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentBehaviorIntroBinding
import com.mozzarelly.cbthelper.databinding.FragmentBehaviorQuestion3RadiosBinding
import com.mozzarelly.cbthelper.databinding.FragmentBehaviorQuestionSliderBinding

class Behavior4Fragment : BehaviorFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentBehaviorQuestionSliderBinding.inflate(inflater, container, false).apply {
            buttons.setupButtons(enableNextWhenFilled = viewModel.rationalValue)
            buttons.next.run {
                text = getString(R.string.finish)

                setOnClickListener {
                    viewModel.save()
                    act.finish(1)
                }
            }

            expression.displayDatum(viewModel.expression)

            slider.bind(viewModel.rationalValue, R.string.behavior4Question, R.string.irrational, R.string.rational)

        }.root
}