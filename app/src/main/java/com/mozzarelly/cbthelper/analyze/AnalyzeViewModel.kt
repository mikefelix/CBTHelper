@file:Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_OVERRIDE")

package com.mozzarelly.cbthelper.analyze

import androidx.lifecycle.*
import com.mozzarelly.cbthelper.*
import com.mozzarelly.cbthelper.map
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class Stage {
    Begun,
    EntryPartial,
    EntryComplete,
    CogValPartial,
    CogValComplete,
    RatRepPartial,
    RatRepComplete,
    BehaviorPartial,
    BehaviorComplete
}

class AnalyzeViewModel : CBTViewModel() {

    private val entryDao by lazy { CBTDatabase.getDatabase(applicationContext).entryDao() }
    private val cogValidDao by lazy { CBTDatabase.getDatabase(applicationContext).cogValidDao() }
    private val ratRepDao by lazy { CBTDatabase.getDatabase(applicationContext).ratRepDao() }
    private val behaviorDao by lazy { CBTDatabase.getDatabase(applicationContext).behaviorDao() }

    var id: Int = 0
        set(value) {
            if (value > 0 && field != value) {
                viewModelScope.launch {
                    try {
                        entry.value = entryDao.get(value)
                        cogValid.value = cogValidDao.get(value)
                        ratRep.value = ratRepDao.get(value)
                        behavior.value = behaviorDao.get(value)
                        calculateStage()
                    }
                    catch (e: Throwable) {
                        e.rethrowIfCancellation()
                    }
                }

                field = value
            }
        }

    val idFlow = MutableStateFlow(0)

    val entryFlow = idFlow.flatMapConcat {
        entryDao.getFlow(it)
    }

    val entry    = MutableLiveData<Entry?>()//idValue.flatMap { entryDao.getAsync(it) }
    val cogValid = MutableLiveData<CogValid?>()//idValue.flatMap { cogValidDao.getAsync(it) }
    val ratRep   = MutableLiveData<RatRep?>()//idValue.flatMap { ratRepDao.getAsync(it) }
    val behavior = MutableLiveData<Behavior?>()//idValue.flatMap { behaviorDao.getAsync(it) }
    val stage    = MutableLiveData<Stage?>()
    
    private fun calculateStage(){
        val entryVal = entry.value
        val cogValidVal = cogValid.value
        val ratRepVal = ratRep.value 
        val behaviorVal = behavior.value 

        stage.value = when {
            behaviorVal.complete() -> Stage.BehaviorComplete
            behaviorVal.started() -> Stage.BehaviorPartial
            ratRepVal.complete() -> Stage.RatRepComplete
            ratRepVal.started() -> Stage.RatRepPartial
            cogValidVal.complete() -> Stage.CogValComplete
            cogValidVal.started() -> Stage.CogValPartial
            entryVal.complete() -> Stage.EntryComplete
            entryVal.started() -> Stage.EntryPartial
            else -> Stage.Begun
        }
    }
    
    private fun Model?.started() = this?.started ?: false
    private fun Model?.complete() = this?.complete ?: false

    val ratRepState = ratRep.map { it?.complete }
    val thinkInstead = ratRep.mapValue { it.thinkInstead }
    val comparison = ratRep.mapValue { it.comparison }
    val possibleEmotions = ratRep.mapValue { emotionText(it.emotion1, it.emotion2, it.emotion3) }
    val otherPeopleRational = behavior.mapValue { it.otherBehaviorRational }

    val feltInstead = Pair(entry, ratRep).mapValues { e, r ->
        emotionTextContrasted(r.emotions, e.emotions, delimiter1 = "and", delimiter2 = ", ")
    }

    val situation = entry.mapValue { it.situation }
    val typeString = entry.mapValueAs { if (situationType) "situation" else "conversation" }
    val typeStringCapitalized = entry.mapValueAs { if (situationType) "Situation" else "Conversation" }
    val emotionsFelt = entry.mapValueAs { emotionString }
    val emotionsFeltSimple = entry.mapValueAs { simpleEmotionString }
    val thoughts = entry.mapValue { it.thoughts }
    val assumptions = entry.mapValue { it.assumptions }
    val expressed = entry.mapValue { it.expression }
    val relationships = entry.mapValue { it.relationships }
    val entryDesc = entry.mapValueAs { "A ${if (situationType) "situation at" else "conversation with" } $whoWhere" }
    val detail = entry.mapValueAs { situation }

    val thinkingErrors = cogValid.map { it?.errors() ?: emptyList() }
    val behavingErrors = behavior.map { it?.errors() ?: emptyList() }
    val moreRationalAction = behavior.mapValue { it.moreRationalActions }
    val othersReactions = behavior.mapValue { it.moreRationalAffect }

/*    fun save(){
        comparison.value?.takeIf { it != ratRep.value?.comparison }?.let {
            viewModelScope.launch {
                val r = ratRep.value ?: RatRep.new(entry.value!!.id).also { ratRepDao.create(it) }
                r.comparison = it
                ratRepDao.update(r)
            }
        }
    }*/
}