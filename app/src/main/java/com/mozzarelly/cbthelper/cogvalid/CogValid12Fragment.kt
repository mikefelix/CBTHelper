package com.mozzarelly.cbthelper.cogvalid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.google.android.material.snackbar.Snackbar
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentCogvalQuestion2RadiosBinding
import com.mozzarelly.cbthelper.databinding.FragmentCogvalQuestionSliderBinding

class CogValid12Fragment : CogValidFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentCogvalQuestionSliderBinding.inflate(inflater, container, false).apply {
            thoughts.text = viewModel.thoughts

            bindQuestion(viewModel.answer12Value,
                R.string.validityQuestion12,
                R.string.validityAnswer12A,
                R.string.validityAnswer12B
            )

            buttons.next.run {
                text = getString(R.string.finish)

                setOnClickListener {
//                    viewModel.complete = true
                    viewModel.save()
                    act.finish(1)
                }
            }
        }.root

}

