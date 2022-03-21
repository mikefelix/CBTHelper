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

    private val entryDao by lazy { CBTDatabase.getDatabase().entryDao() }
    private val cogValidDao by lazy { CBTDatabase.getDatabase().cogValidDao() }
    private val ratRepDao by lazy { CBTDatabase.getDatabase().ratRepDao() }
    private val behaviorDao by lazy { CBTDatabase.getDatabase().behaviorDao() }

    var id: Int
        get() = idFlow.value
        set(value) {
            idFlow.value = value
/*
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
*/
        }

    private val idFlow = MutableStateFlow(0)

    private val entryFlow    = idFlow.filter { it > 0 }.flatMapConcat { entryDao.getFlow(it) }
    private val cogValidFlow = idFlow.filter { it > 0 }.flatMapConcat { cogValidDao.getFlow(it) }
    private val ratRepFlow   = idFlow.filter { it > 0 }.flatMapConcat { ratRepDao.getFlow(it) }
    private val behaviorFlow = idFlow.filter { it > 0 }.flatMapConcat { behaviorDao.getFlow(it) }

    private val analysisFlow    = entryFlow
        .combine(cogValidFlow) { e, c -> e to c }
        .combine(ratRepFlow) { (e, c), r -> Triple(e, c, r) }
        .combine(behaviorFlow) { (e, c, r), b -> e?.let { Analysis(e, c, r, b) } }

//    private val stageFlow    = entryFlow
//        .combine(cogValidFlow) { e, c -> e to c }
//        .combine(ratRepFlow) { (e, c), r -> Triple(e, c, r) }
//        .combine(behaviorFlow) { (e, c, r), b -> calculateStage(e, c, r, b) }

    val analysis = analysisFlow.asLiveData()
    val entry = entryFlow.asLiveData()
    val cogValid = cogValidFlow.asLiveData()
    val ratRep = ratRepFlow.asLiveData()
    val behavior = behaviorFlow.asLiveData()
//    val stage = stageFlow.asLiveData()

    val ratRepState = ratRep.mapValue { it.complete }
    val thinkInstead = ratRep.mapValue { it.thinkInstead }
    val comparison = ratRep.mapValue { it.comparison }
    val possibleEmotions = ratRep.mapValue { emotionText(it.emotion1, it.emotion2, it.emotion3) }
    val otherPeopleRational = behavior.mapValue { it.otherBehaviorRational }

    val feltInstead = Pair(entry, ratRep).map { e, r ->
        emotionTextContrasted(r?.emotions ?: emptyList(), e?.emotions ?: emptyList(), delimiter1 = "and", delimiter2 = ", ")
    }

    val situation = entry.mapValue { it.situation }
    val typeString = entry.mapValue { it.situationTypeText }
    val typeStringCapitalized = entry.mapValue { if (it.situationType) "Situation" else "Conversation" }
    val emotionsFelt = entry.mapValue { it.emotions }
    val emotionsFeltString = entry.mapValue { it.emotionString }
    val emotionsFeltSimple = entry.mapValue { it.simpleEmotionString }
    val thoughts = entry.mapValue { it.thoughts }
    val assumptions = entry.mapValue { it.assumptions }
    val expressed = entry.mapValue { it.expression }
    val relationships = entry.mapValue { it.relationships }
    val entryDesc = entry.mapValue { "A ${if (it.situationType) "situation at" else "conversation with" } ${it.whoWhere}" }
    val detail = entry.mapValue { it.situation }

    val thinkingErrors = cogValid.mapValue { it.errors() }
    val behavingErrors = behavior.mapValue { it.errors() }
    val moreRationalAction = behavior.mapValue { it.moreRationalActions }
    val othersReactions = behavior.mapValue { it.moreRationalAffect }

    init {
        viewModelScope.launch {
            entryFlow.collect {
                println("Entry flow emitted ${it?.id}")
            }
        }
    }

    override fun patientGuidePage() = PatientGuide.Page.FourB2

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

data class Analysis(
    val entry: Entry,
    val cogVal: CogValid?,
    val ratRep: RatRep?,
    val behavior: Behavior?
) {
    val stage: Stage 
        get() = calculateStage(entry, cogVal, ratRep, behavior)
}

private fun Model?.started() = this?.started ?: false
private fun Model?.complete() = this?.complete ?: false

private fun calculateStage(entryVal: Entry?, cogValidVal: CogValid?, ratRepVal: RatRep?, behaviorVal: Behavior?): Stage = when {
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
