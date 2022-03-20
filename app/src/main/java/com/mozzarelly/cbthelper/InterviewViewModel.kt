@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.mozzarelly.cbthelper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch

sealed class SaveResult() {
    object NoChange: SaveResult(){
        override val resultCode: Int = 0
    }

    class SavedPartial(val id: Int): SaveResult(){
        override val resultCode: Int = id
    }

    class SavedComplete(val id: Int): SaveResult(){
        override val resultCode: Int = id + complete
    }

    class Error(val e: Throwable?): SaveResult(){
        override val resultCode: Int = -1
    }

    val saved: Boolean
        get() = this is SavedPartial || this is SavedComplete

    abstract val resultCode: Int

    companion object {
        private const val complete = Int.MAX_VALUE / 2

        fun from(resultCode: Int): SaveResult = when {
            resultCode == 0 -> NoChange
            resultCode < 0 -> Error(null)
            resultCode > complete -> SavedComplete(resultCode - complete)
            else -> SavedPartial(resultCode)
        }
    }
}

data class LateInt(val int: Int)

abstract class InterviewViewModel : CBTViewModel(){

    val page = MutableLiveData<Pair<Int, Int?>>(Pair(0, null))

    val currPage: Int
        get() = page.value?.first ?: 0

    val numPages: Int
        get() = numberOfPages.int

    lateinit var numberOfPages: LateInt
    lateinit var lateId: LateInt

    lateinit var entry: LiveData<Entry?>

    var id: Int
        get() = lateId.int
        set(value){ lateId = LateInt(value) }

    abstract val complete: Boolean
    abstract val title: LiveData<String?>

    val entryDao by lazy { CBTDatabase.getDatabase().entryDao() }

    private val changingPageChannel = BroadcastChannel<Pair<Int, Int?>>(Channel.BUFFERED)
    val changingPage = changingPageChannel.asFlow()

    fun save() {
        viewModelScope.launch {
            saveAsync()
        }
    }

    abstract suspend fun saveAsync(): SaveResult
    abstract fun load(id: Int)

    fun previousPage(){
        changePage(currPage - 1)
        save()
    }

    fun nextPage(){
        changePage(currPage + 1)
        save()
    }

//    fun <V, P: PageFragment<V>> goToPage(clazz: KClass<P>){
//        changePage()
//        save()
//    }

    fun changePage(newPage: Int){
        val oldPage = currPage

        val anim = newPage.compareTo(currPage)

        Pair(newPage.coerceAtLeast(1).coerceAtMost(numPages), anim).let {
            page.value = it

            if (it.first != oldPage)
                changingPageChannel.offer(it)
        }
    }

    fun markEntry(marked: Boolean) {
        viewModelScope.launch {
            entryDao.update(entry.value!!.copy(marked = true))
        }
    }

}