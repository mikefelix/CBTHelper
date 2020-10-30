package com.mozzarelly.cbthelper.editentry

import androidx.fragment.app.activityViewModels
import com.mozzarelly.cbthelper.PageFragment

abstract class AddEntryFragment : PageFragment<EditEntryViewModel>() {
    override val viewModel: EditEntryViewModel by activityViewModels()// { act.viewModelProvider }
}