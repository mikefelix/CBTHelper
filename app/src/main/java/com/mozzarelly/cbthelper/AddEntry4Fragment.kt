package com.mozzarelly.cbthelper

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.databinding.FragmentAdd4ExpressionBinding
import java.util.*

class AddEntry4Fragment(private val viewModel: EditEntryViewModel) : AddEntryFragment(viewModel) {
    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentAdd4ExpressionBinding.inflate(inflater).apply {
            buttons.previous.setOnClickListener {
                previousPage()
            }

            buttons.next.run {
                enableWhenHasValue(viewModel.expressionValue)

                setOnClickListener {
                    nextPage()
                }
            }

            expression.bindTo(viewModel.expressionValue)

            bottledCheck.bindTo(viewModel.bottledValue)

            observe(viewModel.emotionsChosenValue){
                textView1.text = "What did you do and say to show that you felt ${it?.toLowerCase(Locale.US) ?: "your emotions"}?"
            }
        }.root
}