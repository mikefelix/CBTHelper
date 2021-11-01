@file:Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_OVERRIDE")

package com.mozzarelly.cbthelper.analyze

import androidx.lifecycle.*
import com.mozzarelly.cbthelper.*
import com.mozzarelly.cbthelper.map

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

    var id: Int
        get() = idValue.value ?: 0
        set(value) {
            idValue.value = value
        }

    private val idValue = MutableLiveData<Int>()

    val entry = idValue.flatMap { entryDao.getAsync(it) }
    val cogValid = idValue.flatMap { cogValidDao.getAsync(it) }
    val ratRep = idValue.flatMap { ratRepDao.getAsync(it) }
    val behavior = idValue.flatMap { behaviorDao.getAsync(it) }

    val stage: LiveData<Stage?> = mediator(entry, cogValid, ratRep, behavior){
        val entryVal = entry.value ?: return@mediator null
        val cogValidVal = cogValid.value ?: return@mediator null
        val ratRepVal = ratRep.value ?: return@mediator null
        val behaviorVal = behavior.value ?: return@mediator null

        when {
            behaviorVal.complete -> Stage.BehaviorComplete
            behaviorVal.started -> Stage.BehaviorPartial
            ratRepVal.complete -> Stage.RatRepComplete
            ratRepVal.started -> Stage.RatRepPartial
            cogValidVal.complete -> Stage.CogValComplete
            cogValidVal.started -> Stage.CogValPartial
            entryVal.complete -> Stage.EntryComplete
            entryVal.started -> Stage.EntryPartial
            else -> Stage.Begun
        }
    }

    val ratRepState = ratRep.map { it?.complete }
    val thinkInstead = ratRep.mapValue { it.thinkInstead }
    val comparison = ratRep.mapValue { it.comparison }
    val possibleEmotions = ratRep.mapValue { emotionText(it.emotion1, it.emotion2, it.emotion3) }
    val otherPeopleRational = behavior.mapValue { it.otherBehaviorRational }

    val feltInstead = Pair(entry, ratRep).mapValues { e, r ->
        emotionTextContrasted(r.emotions, e.emotions, delimiter1 = "and", delimiter2 = ", ")
    }

    val situation = entry.mapValue { it.situation }
    val typeString = entry.mapValueAs { if (situationType) "Situation" else "Conversation" }
    val emotionsFelt = entry.mapValueAs { emotionString }
    val emotionsFeltSimple = entry.mapValueAs { simpleEmotionString }
    val thoughts = entry.mapValue { it.thoughts }
    val assumptions = entry.mapValue { it.assumptions }
    val expressed = entry.mapValue { it.expression }
    val relationships = entry.mapValue { it.relationships }
    val entryDesc = entry.mapValueAs { "A ${if (situationType) "situation at" else "conversation with" } $whoWhere" }
    val detail = entry.mapValueAs { situation }

    val thinkingErrors = cogValid.map { it?.errors() ?: emptyList() }
    val behavingErrors = behavior.map {
        it?.errors() ?: emptyList()
    }
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