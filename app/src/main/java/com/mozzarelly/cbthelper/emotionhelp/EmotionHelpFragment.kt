package com.mozzarelly.cbthelper.emotionhelp

import androidx.fragment.app.activityViewModels
import com.mozzarelly.cbthelper.PageFragment

abstract class EmotionHelpFragment : PageFragment<EmotionHelpViewModel>() {
    override val viewModel: EmotionHelpViewModel by activityViewModels()

}