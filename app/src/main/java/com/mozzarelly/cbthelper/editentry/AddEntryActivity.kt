package com.mozzarelly.cbthelper.editentry

import androidx.lifecycle.viewModelScope
import com.mozzarelly.cbthelper.LateInt
import com.mozzarelly.cbthelper.InterviewActivity
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.cbtViewModel
import kotlinx.coroutines.launch

class AddEntryActivity : InterviewActivity<EditEntryViewModel>() {

    override val viewModel: EditEntryViewModel by cbtViewModel()

    override val fragmentTypes = listOf(
        AddEntry1Fragment::class,
        AddEntry2Fragment::class,
        AddEntry3Fragment::class,
        AddEntry4Fragment::class,
        AddEntry5Fragment::class,
        AddEntry6Fragment::class
    )

    override fun EditEntryViewModel.setup() {
        numberOfPages = LateInt(fragmentTypes.size)

        viewModelScope.launch {
            when {
                intent.hasExtra("forceNew") -> loadNewEntry()
                intent.hasExtra("id") -> loadEntry(getIdExtra())
                dao.getIncomplete() != null -> loadEntryInProgress()
                else -> loadNewEntry()
            }
        }
    }
}
