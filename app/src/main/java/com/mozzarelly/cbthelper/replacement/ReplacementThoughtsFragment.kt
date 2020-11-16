package com.mozzarelly.cbthelper.replacement

import androidx.fragment.app.activityViewModels
import com.mozzarelly.cbthelper.PageFragment

abstract class ReplacementThoughtsFragment : PageFragment<ReplacementThoughtsViewModel>() {
    override val viewModel: ReplacementThoughtsViewModel by activityViewModels()


}