package com.mozzarelly.cbthelper.behavior

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentBehaviorQuestionSliderBinding
import com.mozzarelly.cbthelper.map

class Behavior4Fragment : BehaviorFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentBehaviorQuestionSliderBinding.inflate(inflater, container, false).apply {
            buttons.previous.setOnClickListener { previousPage() }
            buttons.next.run {
                enableWhenHasValue(viewModel.otherPersonRationalValue)
                setOnClickListener {
                    viewModel.save()
                    if (viewModel.errors().isEmpty()) {
                        act.finish(1)
                    }
                    else {
                        nextPage()
                    }
                }
            }

            answer1.display(viewModel.relationshipsValue.map { it.describeWith(getString(R.string.behavior3Question1)) })
            answer2.display(viewModel.healthValue.map { it.describeWith(getString(R.string.behavior3Question2)) })
            answer3.display(viewModel.occupationsValue.map { it.describeWith(getString(R.string.behavior3Question3)) })
            answer4.display(viewModel.otherValue.map { it.describeWith(getString(R.string.behavior3Question4)) })

            expression.display(viewModel.expression)

//            slider.bind(viewModel.rationalValue, R.string.behavior4Question, R.string.irrational, R.string.rational)

        }.root
}

fun Int?.describeWith(string: String): String {
    val effect = when (this) {
        1 -> "more benefit"
        2 -> "more harm"
        3 -> "neither more benefit nor more harm"
        else -> "[question not answered]" // Shouldn't happen
    }

    return string.replace(":$".toRegex(), " ") + effect + " for me."
}