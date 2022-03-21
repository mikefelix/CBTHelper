package com.mozzarelly.cbthelper.viewentries

import androidx.lifecycle.viewModelScope
import com.mozzarelly.cbthelper.CBTDatabase
import com.mozzarelly.cbthelper.CBTViewModel
import com.mozzarelly.cbthelper.Entry
import com.mozzarelly.cbthelper.PatientGuide
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EntriesViewModel : CBTViewModel() {
    private val dao by lazy { CBTDatabase.getDatabase().entryDao() }

    val allEntries: StateFlow<List<Entry>> by lazy {
        dao.getAllCompleteAsync().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    }

    val incompleteEntry: StateFlow<Entry?> by lazy {
        dao.getIncompleteAsync().stateIn(viewModelScope, SharingStarted.Eagerly, null)
    }

    fun load() {
       /* viewModelScope.launch {
            try {
                incompleteEntry.value = dao.getIncomplete()
            }
            catch (e: Exception) {
                e.rethrowIfCancellation()
                e.printStackTrace()
            }
        }*/
    }

    fun delete(entry: Entry) {
        viewModelScope.launch {
            dao.delete(entry.id)
        }
    }

    fun undelete(entry: Entry) {
        viewModelScope.launch {
            dao.undelete(entry.id)
        }
    }

    override fun patientGuidePage() = PatientGuide.Page.Intro
}