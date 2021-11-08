package com.mozzarelly.cbthelper.viewentries

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mozzarelly.cbthelper.CBTDatabase
import com.mozzarelly.cbthelper.CBTViewModel
import com.mozzarelly.cbthelper.Entry
import kotlinx.coroutines.launch

class EntriesViewModel : CBTViewModel() {

    private val dao by lazy { CBTDatabase.getDatabase().entryDao() }

    val allEntries: LiveData<List<Entry>> by lazy { dao.getAllCompleteAsync() }
    val incompleteEntry: LiveData<Entry?> by lazy { dao.getIncompleteAsync() }

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
}