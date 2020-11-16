package com.mozzarelly.cbthelper.editentry

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.databinding.FragmentAdd4ExpressionBinding
import com.mozzarelly.cbthelper.observe
import java.util.*

class AddEntry4Fragment : AddEntryFragment() {
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

            observe(viewModel.emotionSelection.emotionsChosenSimple){
                textView1.text = "What did you do and say that showed you felt ${it?.toLowerCase(Locale.US) ?: "your emotions"}?"
            }
        }.root
}