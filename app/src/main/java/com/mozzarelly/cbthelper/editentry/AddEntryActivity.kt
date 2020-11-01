package com.mozzarelly.cbthelper.editentry

import androidx.activity.viewModels
import androidx.lifecycle.viewModelScope
import com.mozzarelly.cbthelper.*
import kotlinx.coroutines.launch

class AddEntryActivity : PagingActivity<EditEntryViewModel>() {

    override val layout = R.layout.activity_add_entry

    override val viewModel: EditEntryViewModel by viewModels { viewModelProvider }

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
