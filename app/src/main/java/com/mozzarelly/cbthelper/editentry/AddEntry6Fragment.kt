package com.mozzarelly.cbthelper.editentry

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentAdd6AssumptionsBinding

class AddEntry6Fragment : AddEntryFragment() {
    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentAdd6AssumptionsBinding.inflate(inflater).apply {
            buttons.previous.setOnClickListener {
                previousPage()
            }

            buttons.next.run {
                text = getString(R.string.finish)

                enableWhenHasValue(viewModel.assumptionsValue)

                setOnClickListener {
                    viewModel.complete = true
                    viewModel.save()
                    act.finish(1)
                }
            }

            assumptions.bindTo(viewModel.assumptionsValue)
        }.root
}