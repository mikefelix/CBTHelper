package com.mozzarelly.cbthelper.viewentries

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mozzarelly.cbthelper.*
import kotlinx.coroutines.launch

class EntriesViewModel : CBTViewModel() {

    private val dao by lazy { CBTDatabase.getDatabase(applicationContext).entryDao() }

    val allEntries: /* = *MutableLiveData<List<Entry>>()*/ LiveData<List<Entry>> by lazy { dao.getAllCompleteAsync() }
    val incompleteEntry = MutableLiveData<Entry?>() //: LiveData<Entry?> by lazy { dao.getIncompleteAsync() } //= MutableLiveData()

    fun cleanDatabase(){
        viewModelScope.launch {
            try {
                with (CBTDatabase.getDatabase(applicationContext)){
                    applicationContext.clean(BuildConfig.DEBUG)
                }
            }
            catch (e: Exception) {
                e.rethrowIfCancellation()
                e.printStackTrace()
            }
        }
    }

    fun load() {
       /* viewModelScope.launch {
            try {
                allEntries.value = dao.getAllComplete()
            }
            catch (e: Exception) {
                e.rethrowIfCancellation()
                e.printStackTrace()
            }
        }*/

        viewModelScope.launch {
            try {
                incompleteEntry.value = dao.getIncomplete()
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

    fun undelete(entry: Entry) {
        viewModelScope.launch {
            dao.undelete(entry.id)
        }
    }
}