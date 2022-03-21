@file:Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_OVERRIDE")

package com.mozzarelly.cbthelper.behavior

import androidx.lifecycle.*
import com.mozzarelly.cbthelper.*
import com.mozzarelly.cbthelper.SaveResult.SavedComplete
import com.mozzarelly.cbthelper.SaveResult.SavedPartial
import com.mozzarelly.cbthelper.map
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BehaviorViewModel : InterviewViewModel(), BehaviorModel {

    private val behaviorDao by lazy { CBTDatabase.getDatabase().behaviorDao() }
    private val cogValDao by lazy { CBTDatabase.getDatabase().cogValidDao() }
    private val ratRepDao by lazy { CBTDatabase.getDatabase().ratRepDao() }

    val reacted = MutableLiveData<String?>()
    val situationType = MutableLiveData<String>()
    val feltEmotions = MutableLiveData<String>()
    val feltEmotionsSimple = MutableLiveData<String>()
    val expression = MutableLiveData<String>()
    val insteadEmotions = MutableLiveData<String>()
    val cogValRational = MutableLiveData<Boolean>()
    val replacedThoughts = MutableLiveData<String?>()

    override val title: LiveData<String?> = page.map {
        when (it.first) {
            1 -> "Behavior"
            else -> "Behavior - ${it.first - 1}/${numPages - 1}"
        }
    }

    override var honest: Int?
        get() = honestValue.value
        set(value){
            honestValue.value = value
        }
    override var person: String?
        get() = personValue.value
        set(value){
            personValue.value = value
        }
    override var disappointed: Int?
        get() = disappointedValue.value
        set(value){
            disappointedValue.value = value
        }
    override var disapprove: Int?
        get() = disapproveValue.value
        set(value){
            disapproveValue.value = value
        }
    override var embarrassed: Int?
        get() = embarrassedValue.value
        set(value){
            embarrassedValue.value = value
        }
    override var relationships: Int?
        get() = relationshipsValue.value
        set(value) {
            relationshipsValue.value = value
        }
    override var occupations: Int?
        get() = occupationsValue.value
        set(value) {
            occupationsValue.value = value
        }
    override var health: Int?
        get() = healthValue.value
        set(value) {
            healthValue.value = value
        }
    override var other: Int?
        get() = otherValue.value
        set(value) {
            otherValue.value = value
        }
    override var otherPersonRational: Int?
        get() = otherPersonRationalValue.value
        set(value) {
            otherPersonRationalValue.value = value
        }
    override var moreRationalActions: String?
        get() = moreRationalActionsValue.value
        set(value) {
            moreRationalActionsValue.value = value
        }
    override var moreRationalAffect: String?
        get() = moreRationalAffectValue.value
        set(value) {
            moreRationalAffectValue.value = value
        }
    override var otherBehaviorRational: Int?
        get() = otherBehaviorRationalValue.value
        set(value) {
            otherBehaviorRationalValue.value = value
        }

    override fun load(id: Int) {
        this.id = id
        viewModelScope.launch {
            entryDao.getFlow(id).collect { entry ->
                entry ?: return@collect
                reacted.value = entry.relationships
                situationType.value = entry.situationTypeText
                feltEmotions.value = entry.emotionStringWithNewlines ?: "the emotions you felt"
                feltEmotionsSimple.value = entry.simpleEmotionString?.lowercase() ?: "the emotions you felt"
                expression.value = entry.expression ?: "[text not found]"
            }
        }

        // Rework with Flow?
        viewModelScope.launch {
            (entryDao.get(id) ?: error("Can't find entry $id")).let { entry ->
                reacted.value = entry.relationships
                situationType.value = entry.situationTypeText
                feltEmotions.value = entry.emotionStringWithNewlines ?: "the emotions you felt"
                feltEmotionsSimple.value = entry.simpleEmotionString?.lowercase() ?: "the emotions you felt"
                expression.value = entry.expression ?: "[text not found]"
            }
        }

        viewModelScope.launch {
            val cogVal = cogValDao.get(id) ?: CogValid.new(id).also { cogValDao.create(it) }
            cogValRational.value = cogVal.isRational
        }

        viewModelScope.launch {
            val ratRep = ratRepDao.get(id) ?: RatRep.new(id).also { ratRepDao.create(it) }
            insteadEmotions.value = ratRep.simpleEmotionString ?: "the emotions you should have felt"
            replacedThoughts.value = ratRep.thinkInstead
        }

        viewModelScope.launch {
            copyFrom((behaviorDao.get(id) ?: Behavior.new(id).also { behaviorDao.create(it) }))

            changePage(when {
                moreRationalAffect != null -> 15
                moreRationalActions != null -> 14
                other != null -> 12
                occupations != null -> 11
                health != null -> 10
                relationships != null -> 9
                honest != null -> 8
                embarrassed != null -> 7
                disapprove != null -> 6
                disappointed != null -> 5
                person != null -> 4
                else -> 1
            })
        }
    }

    override suspend fun saveAsync(): SaveResult {
        behaviorDao.update(Behavior.from(this@BehaviorViewModel))
        return if (complete) SavedComplete(id) else SavedPartial(id)
    }

    override val complete
        get() = otherBehaviorRational != null

    val honestValue = MutableLiveData<Int?>()
    val personValue = MutableLiveData<String?>()
    val disappointedValue = MutableLiveData<Int?>()
    val disapproveValue = MutableLiveData<Int?>()
    val embarrassedValue = MutableLiveData<Int?>()
    val relationshipsValue = MutableLiveData<Int?>()
    val occupationsValue = MutableLiveData<Int?>()
    val healthValue = MutableLiveData<Int?>()
    val otherValue = MutableLiveData<Int?>()
    val otherPersonRationalValue = MutableLiveData<Int?>()
    val moreRationalActionsValue = MutableLiveData<String?>()
    val moreRationalAffectValue = MutableLiveData<String?>()
    val otherBehaviorRationalValue = MutableLiveData<Int?>()
    val errors = mediator(disapproveValue, disappointedValue, honestValue, embarrassedValue, healthValue, occupationsValue, relationshipsValue, otherValue){
        errors()
    }

    var skipTest = false

    override fun patientGuidePage() = PatientGuide.Page.ThreeD
}
