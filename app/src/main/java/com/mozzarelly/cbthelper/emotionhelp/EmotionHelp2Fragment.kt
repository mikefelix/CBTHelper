package com.mozzarelly.cbthelper.emotionhelp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.*
import com.mozzarelly.cbthelper.databinding.FragmentEmhelp2Binding
import com.mozzarelly.cbthelper.databinding.FragmentEmhelpBeliefsAnalysisPopupBinding

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

            observe(viewModel.desiredEmotion, viewModel.situationType){ emotion, type ->
                question.display(R.string.emhelp2Question,
                    emotion?.emotion?.lowercase() ?: "that emotion",
                    type ?: "situation")
            }

            answer.bindTo(viewModel.thinkAndBelieve)
        }.root
}