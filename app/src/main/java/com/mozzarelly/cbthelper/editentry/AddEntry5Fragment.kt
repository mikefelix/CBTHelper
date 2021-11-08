package com.mozzarelly.cbthelper.editentry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentAdd5RelationshipsBinding

class AddEntry5Fragment : AddEntryFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentAdd5RelationshipsBinding.inflate(inflater).apply {
            buttons.previous.setOnClickListener {
                previousPage()
            }

            buttons.next.run {
                enableWhenHasValue(viewModel.relationshipsValue)

                setOnClickListener {
                    nextPage()
                }
            }

            relationships.bindTo(viewModel.relationshipsValue)

            textView1.display(getString(if (viewModel.bottled)
                R.string.relationshipsQuestionBottled
            else
                R.string.relationshipsQuestion
            ))

        }.root

}
