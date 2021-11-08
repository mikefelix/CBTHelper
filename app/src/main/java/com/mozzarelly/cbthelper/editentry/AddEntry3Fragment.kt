package com.mozzarelly.cbthelper.editentry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.databinding.FragmentAdd3ThoughtsBinding
import java.util.*

class AddEntry3Fragment : AddEntryFragment() {
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

            textView1.display(viewModel.emotionsChosenSimple) {
                """What were you thinking that led you to feel ${it?.lowercase(Locale.US) ?: "your emotions"}?"""
            }

        }.root

}