package com.mozzarelly.cbthelper.editentry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentAdd1SituationBinding
import com.mozzarelly.cbthelper.observe

class AddEntry1Fragment : AddEntryFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentAdd1SituationBinding.inflate(inflater, container, false).apply {
            buttons.previous.run {
                visibility = View.INVISIBLE

                setOnClickListener {
                    error("Can't go that way.")
                }
            }

            buttons.next.run {
                enableWhenHasValue(viewModel.whoWhereValue, viewModel.situationValue)

                setOnClickListener {
                    nextPage()
                }
            }

            observe(viewModel.situationTypeValue){
                if (it)
                    textView2.display(R.string.situationQuestion)
                else
                    textView2.display(R.string.conversationQuestion)
            }

            situationDesc.bindTo(viewModel.situationValue)
            whoWhere.bindTo(viewModel.whoWhereValue)

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
