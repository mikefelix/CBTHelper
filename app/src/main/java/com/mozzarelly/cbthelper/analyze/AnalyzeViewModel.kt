@file:Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_OVERRIDE")

package com.mozzarelly.cbthelper.analyze

import androidx.lifecycle.*
import com.mozzarelly.cbthelper.*
import kotlinx.coroutines.launch

enum class Stage {
    Begun,
    EntryComplete,
    CogValComplete,
    RatRepComplete,
    BehaviorComplete
}

class AnalyzeViewModel : CBTViewModel() {

    val entryDao by lazy { CBTDatabase.getDatabase(applicationContext).entryDao() }
    val cogValidDao by lazy { CBTDatabase.getDatabase(applicationContext).cogValidDao() }
    val ratRepDao by lazy { CBTDatabase.getDatabase(applicationContext).ratRepDao() }
    val behaviorDao by lazy { CBTDatabase.getDatabase(applicationContext).behaviorDao() }

    var id: Int
        get() = idValue.value ?: 0
        set(value){
            idValue.value = value
            viewModelScope.launch {
                entry.value = entryDao.get(value)
            }
            viewModelScope.launch {
                cogValid.value = cogValidDao.get(value)
            }
            viewModelScope.launch {
                ratRep.value = ratRepDao.get(value).also {
                    comparison.value = it?.comparison
                }
            }
            viewModelScope.launch {
                behavior.value = behaviorDao.get(value)
            }
        }

    private val idValue = MutableLiveData<Int>()

    val entry = MutableLiveData<Entry?>()
    val cogValid = MutableLiveData<CogValid?>()
    val ratRep = MutableLiveData<RatRep?>()
    val behavior = MutableLiveData<Behavior?>()

    val stage = MediatorLiveData<Stage>().apply {
        val observer = Observer<Any?> {
            value = when {
                behavior.value?.complete == true -> Stage.BehaviorComplete
                ratRep.value?.complete == true -> Stage.RatRepComplete
                cogValid.value?.complete == true -> Stage.CogValComplete
                entry.value?.complete == true -> Stage.EntryComplete
                else -> Stage.Begun
            }
        }

        addSource(entry, observer)
        addSource(cogValid, observer)
        addSource(ratRep, observer)
        addSource(behavior, observer)
    }

    val situation = entry.mapValue {
        it.situation
    }

    val typeString = entry.mapValueAs { if (situationType) "Situation" else "Conversation" }

    val emotionsChosen = entry.mapValueAs { emotionString }

    val thinkingErrors = cogValid.mapValue {
        it.thinkingErrors()
    }

    val thoughts = entry.mapValue { it.thoughts }
    val instead = ratRep.mapValue { it.instead }
    val actualEmotions = entry.mapValue { emotionText(it.emotion1Pair, it.emotion2Pair, it.emotion3Pair) }
    val expressed = entry.mapValue { it.expression }
    val relationships = entry.mapValue { it.relationships }
    val possibleEmotions = ratRep.mapValue {
        emotionText(it.emotion1Pair, it.emotion2Pair, it.emotion3Pair)
    }

    val comparison = MutableLiveData<String?>()

    fun save(){
        comparison.value?.takeIf { it != ratRep.value?.comparison }?.let {
            viewModelScope.launch {
                val r = ratRep.value ?: RatRep.new(entry.value!!.id).also { ratRepDao.create(it) }
                r.comparison = it
                ratRepDao.update(r)
            }
        }
    }
}