package com.mozzarelly.cbthelper.emotionhelp

import com.mozzarelly.cbthelper.InterviewActivity
import com.mozzarelly.cbthelper.LateInt
import com.mozzarelly.cbthelper.cbtViewModel

class EmotionHelpActivity : InterviewActivity<EmotionHelpViewModel>() {

    override val fragmentTypes = listOf(
        EmotionHelp1Fragment::class,
        EmotionHelp2Fragment::class
    )

    override val viewModel: EmotionHelpViewModel by cbtViewModel()

    override fun EmotionHelpViewModel.setup() {
        numberOfPages = LateInt(fragmentTypes.size)
        load(getIdExtra())
    }

}