package com.mozzarelly.cbthelper.emotionhelp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.RESULT_EXIT_TO_MAIN
import com.mozzarelly.cbthelper.databinding.FragmentEmhelp2Binding
import com.mozzarelly.cbthelper.databinding.FragmentEmhelpBeliefsAnalysisPopupBinding
import com.mozzarelly.cbthelper.emotionTextSimple
import com.mozzarelly.cbthelper.makeBulletedList

class EmotionHelp2Fragment : EmotionHelpFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentEmhelp2Binding.inflate(inflater, container, false).apply {
            buttons.setupButtons(viewModel.thinkAndBelieve)

            helpButton.setOnClickListener {
                showInBottomSheet(FragmentEmhelpBeliefsAnalysisPopupBinding.inflate(layoutInflater, null, false)){ dialog ->
                    text1.display(R.string.beliefsAnalysisText1)
                    example1.display(R.string.beliefsAnalysisExample1)
                    text2.display(R.string.beliefsAnalysisText2)
                    text3.display(R.string.beliefsAnalysisText3)
                    text4.display(R.string.beliefsAnalysisText4)
                    text5.display(R.string.beliefsAnalysisText5)
                    textBullets.display(makeBulletedList(8, requireContext(), R.string.beliefsAnalysisBullet1, R.string.beliefsAnalysisBullet2))
                    beliefs.display(viewModel.assumptions)
                    markBox.setOnCheckedChangeListener { _, isChecked -> viewModel.markEntry(isChecked) }
                    continueBtn.setOnClickListener { dialog.dismiss() }
                    exitBtn.setOnClickListener { act.finish(RESULT_EXIT_TO_MAIN) }
                }
            }

            question.display(R.string.emhelp2Question,
                emotionTextSimple(viewModel.desiredEmotion.value)?.lowercase() ?: "that emotion",
                viewModel.situationType.value ?: "situation")

            answer.bindTo(viewModel.thinkAndBelieve)
        }.root
}