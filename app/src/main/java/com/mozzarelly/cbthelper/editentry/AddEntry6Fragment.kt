package com.mozzarelly.cbthelper.editentry

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentAdd6AssumptionsBinding
import com.mozzarelly.cbthelper.observe

class AddEntry6Fragment : AddEntryFragment() {
    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        FragmentAdd6AssumptionsBinding.inflate(inflater).apply {
            question.display(R.string.assumptionsQuestion, if (viewModel.situationType) "in this situation" else "during this conversation")

            readMore.setOnClickListener {
                showExplanationPopup(R.string.assumptionsReadMoreHeading, R.string.assumptionsReadMore)
            }

            buttons.previous.setOnClickListener {
                previousPage()
            }

            buttons.next.run {
                text = getString(R.string.finish)

                enableWhenHasValue(viewModel.assumptionsValue)

                setOnClickListener {
                    viewModel.complete = true
                    viewModel.save()
                    act.finish(viewModel.id)
                }
            }

            assumptions.bindTo(viewModel.assumptionsValue)
        }.root
}