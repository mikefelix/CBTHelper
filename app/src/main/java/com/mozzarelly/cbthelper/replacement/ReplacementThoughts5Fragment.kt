package com.mozzarelly.cbthelper.replacement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentReplacement3Binding
import com.mozzarelly.cbthelper.databinding.FragmentReplacement4ImprovedExpressionBinding
import com.mozzarelly.cbthelper.databinding.FragmentReplacement5ImaginedFutureBinding
import com.mozzarelly.cbthelper.databinding.FragmentReplacement5ImprovedExpressionBinding

class ReplacementThoughts5Fragment : ReplacementThoughtsFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentReplacement5ImaginedFutureBinding.inflate(inflater, container, false).apply {
            text1.display(R.string.imaginedFuture1)
            text2.display(R.string.imaginedFuture2)
            text3.display(R.string.imaginedFuture3, viewModel.situationType.value ?: "situation")
            text4.display(R.string.imaginedFuture4)
            text5.display(R.string.imaginedFuture5, viewModel.situationType.value ?: "situation")
            text6.display(R.string.imaginedFuture6)
            text7.display(R.string.imaginedFuture7)
            text8.display(R.string.imaginedFuture8)

            replacementThoughts.display(viewModel.insteadValue)

            finishButton.setOnClickListener {
                viewModel.save()
                act.finish(1)
            }
        }.root
}
