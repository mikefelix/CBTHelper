@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.mozzarelly.cbthelper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow

data class LateInt(val int: Int)

abstract class InterviewViewModel : CBTViewModel(){

    val page = MutableLiveData<Pair<Int, Int?>>(Pair(0, null))

    val numPages: Int
        get() = numberOfPages.int

    lateinit var numberOfPages: LateInt

    abstract val complete: Boolean
    abstract val title: LiveData<String?>

    private val changingPageChannel = BroadcastChannel<Pair<Int, Int?>>(Channel.BUFFERED)
    val changingPage = changingPageChannel.asFlow()

    val currPage: Int
        get() = page.value?.first ?: 0

    abstract fun save()

    fun previousPage(){
        changePage(currPage - 1)
        save()
    }

    fun nextPage(){
        changePage(currPage + 1)
        save()
    }

    protected fun changePage(newPage: Int){
        val oldPage = currPage

        val anim = newPage.compareTo(currPage)

        Pair(newPage.coerceAtLeast(1).coerceAtMost(numPages), anim).let {
            page.value = it

            if (it.first != oldPage)
                changingPageChannel.offer(it)
        }
    }
}