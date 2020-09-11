package com.mozzarelly.cbthelper

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.mozzarelly.cbthelper.databinding.FragmentAdd4ExpressionBinding
import com.mozzarelly.cbthelper.databinding.FragmentAdd6AssumptionsBinding

class AddEntry6Fragment(private val viewModel: EditEntryViewModel) : AddEntryFragment(viewModel) {
    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentAdd6AssumptionsBinding.inflate(inflater).apply {
            buttons.previous.setOnClickListener {
                previousPage()
            }

            buttons.next.run {
                text = "Finish"

                enableWhenHasValue(viewModel.assumptionsValue)

                setOnClickListener {
                    viewModel.complete = true
                    viewModel.save()
                    Snackbar.make(root, "Entry complete!", 2000).show()
                    act.finish()
                }
            }

            assumptions.bindTo(viewModel.assumptionsValue)
        }.root
}