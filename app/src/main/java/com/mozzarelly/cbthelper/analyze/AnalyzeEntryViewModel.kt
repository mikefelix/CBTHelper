@file:Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_OVERRIDE")

package com.mozzarelly.cbthelper.analyze

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.mozzarelly.cbthelper.*

class AnalyzeEntryViewModel : CBTViewModel() {

    val entryDao by lazy { CBTDatabase.getDatabase(applicationContext).entryDao() }
    val cogValidDao by lazy { CBTDatabase.getDatabase(applicationContext).cogValidDao() }

    val title = MutableLiveData("Analyze entry")

    var id: Int
        get() = idValue.value ?: 0
        set(value){
            idValue.value = value
        }

    private val idValue = MutableLiveData<Int>()

    val entry = idValue.switchMap {
        liveData {
            emit(entryDao.get(it))
        }
    }

    val cogValid = idValue.switchMap {
        liveData {
            emit(cogValidDao.get(it))
        }
    }

    val situation = entry.mapValue {
        it.situation
    }

    val emotionsChosen = entry.mapValue {
        emotionText(it.emotion1, it.emotion2, it.emotion3)
    }

    val thinkingErrors = cogValid.mapValue {
        it.thinkingErrors()
    }
}