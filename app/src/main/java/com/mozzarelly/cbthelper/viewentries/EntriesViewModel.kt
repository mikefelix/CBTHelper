package com.mozzarelly.cbthelper.viewentries

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mozzarelly.cbthelper.CBTDatabase
import com.mozzarelly.cbthelper.CBTViewModel
import com.mozzarelly.cbthelper.Entry
import com.mozzarelly.cbthelper.rethrowIfCancellation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class EntriesViewModel : CBTViewModel() {

    val dao by lazy { CBTDatabase.getDatabase(applicationContext).entryDao() }

    val allEntries: LiveData<List<Entry>> = MutableLiveData()

    val incompleteEntry: Flow<Entry?> = flow {
        emit(dao.getIncomplete())
    }

    // Just a place to poke, to collapse the lazy waveform
    fun load() {}

    fun loadEntries(){
        viewModelScope.launch {
            try {
                (allEntries as MutableLiveData).value = dao.getAllComplete()
            }
            catch (e: Exception) {
                e.rethrowIfCancellation()
                e.printStackTrace()
            }
        }
    }

    fun delete(entry: Entry) {
        viewModelScope.launch {
            dao.delete(entry.id)
        }
    }
}