package com.mozzarelly.cbthelper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.databinding.FragmentAdd1SituationBinding

class AddEntry1Fragment(private val viewModel: EditEntryViewModel) : AddEntryFragment(viewModel) {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentAdd1SituationBinding.inflate(inflater, container, false).apply {
            buttons.previous.run {
                visibility = View.INVISIBLE

                setOnClickListener {
                    error("Can't go that way.")
                }
            }

            buttons.next.run {
                enableWhenHasValue(viewModel.situationValue)

                setOnClickListener {
                    nextPage()
                }
            }

            observe(viewModel.situationTypeValue){
                if (it)
                    textView2.setText(R.string.situationQuestion)
                else
                    textView2.setText(R.string.conversationQuestion)
            }

            situationDesc.bindTo(viewModel.situationValue)
            whoWhere.bindTo(viewModel.situationDetailValue)

            viewLifecycleOwner.observe(viewModel.situationTypeValue) {
                if (it)
                    radioSituation.isChecked = true
                else
                    radioConversation.isChecked = true
            }

            radioSituation.setOnCheckedChangeListener { _, checked ->
                if (checked)
                    viewModel.situationTypeValue.value = true
            }

            radioConversation.setOnCheckedChangeListener { _, checked ->
                if (checked)
                    viewModel.situationTypeValue.value = false
            }
        }.root

}
