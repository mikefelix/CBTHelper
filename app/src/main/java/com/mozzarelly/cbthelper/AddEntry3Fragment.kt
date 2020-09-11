package com.mozzarelly.cbthelper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.databinding.FragmentAdd3ThoughtsBinding
import java.util.*

class AddEntry3Fragment(private val viewModel: EditEntryViewModel) : AddEntryFragment(viewModel) {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentAdd3ThoughtsBinding.inflate(inflater).apply {
            buttons.previous.setOnClickListener {
                previousPage()
            }

            buttons.next.run {
                enableWhenHasValue(viewModel.thoughtsValue)

                setOnClickListener {
                    nextPage()
                }
            }

            thoughts.bindTo(viewModel.thoughtsValue)

            viewModel.emotionsChosenValue.showValueIn(textView1) {
                """What were you thinking that led you to feel ${it?.toLowerCase(Locale.US) ?: "your emotions"}?"""
            }

        }.root

}