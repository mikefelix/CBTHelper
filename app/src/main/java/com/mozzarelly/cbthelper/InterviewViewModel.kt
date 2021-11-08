@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.mozzarelly.cbthelper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

data class LateInt(val int: Int)

abstract class InterviewViewModel : CBTViewModel(){

    val page = MutableLiveData<Pair<Int, Int?>>(Pair(0, null))

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

    val currPage: Int
        get() = page.value?.first ?: 0

    abstract fun save()
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