@file:Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_OVERRIDE")

package com.mozzarelly.cbthelper.analyze

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.mozzarelly.cbthelper.CBTDatabase
import com.mozzarelly.cbthelper.CBTViewModel
import com.mozzarelly.cbthelper.emotionText
import com.mozzarelly.cbthelper.map

class AnalyzeEntryViewModel : CBTViewModel() {

    val dao by lazy { CBTDatabase.getDatabase(applicationContext).entryDao() }

    var id: Int
        get() = idValue.value ?: 0
        set(value){
            idValue.value = value
        }

    private val idValue = MutableLiveData<Int>()

    val entry = idValue.switchMap {
        liveData {
            emit(dao.get(it))
        }
    }

    val situation = entry.map {
        it.situation
    }

    val emotionsChosen = entry.map {
        emotionText(it.emotion1, it.emotion2, it.emotion3)
    }

}