package com.mozzarelly.cbthelper.behavior

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentBehaviorIntroBinding

class BehaviorIntroFragment : BehaviorFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentBehaviorIntroBinding.inflate(inflater, container, false).apply {
            buttons.setupButtons()
            emotions.display(viewModel.emotions)
            expression.display(viewModel.expression)
            intro1.display(R.string.behaviorIntro1)
            intro2.display(R.string.behaviorIntro2)
            instructions.display(R.string.behaviorPrompt)
        }.root
}