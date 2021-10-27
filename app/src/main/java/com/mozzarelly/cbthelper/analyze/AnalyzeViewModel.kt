@file:Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_OVERRIDE")

package com.mozzarelly.cbthelper.analyze

import androidx.lifecycle.*
import com.mozzarelly.cbthelper.*
import com.mozzarelly.cbthelper.map
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

    val entryDao by lazy { CBTDatabase.getDatabase(applicationContext).entryDao() }
    val cogValidDao by lazy { CBTDatabase.getDatabase(applicationContext).cogValidDao() }
    val ratRepDao by lazy { CBTDatabase.getDatabase(applicationContext).ratRepDao() }
    val behaviorDao by lazy { CBTDatabase.getDatabase(applicationContext).behaviorDao() }

    var id: Int
        get() = idValue.value ?: 0
        set(value) {
            idValue.value = value
            init()
        }

    fun init(){
        val value = idValue.value?.takeIf { it > 0 } ?: throw RuntimeException("Don't init with 0 ID!")

        entry =  entryDao.getAsync(value)
        cogValid = cogValidDao.getAsync(value)
        ratRep = ratRepDao.getAsync(value)
        behavior = behaviorDao.getAsync(value)

        ratRepState = ratRep.map { it?.complete }
        situation = entry.mapValue { it.situation }
        typeString = entry.mapValueAs { if (situationType) "Situation" else "Conversation" }
        emotionsChosen = entry.mapValueAs { emotionString }
        thinkingErrors = cogValid.map { it?.thinkingErrors() ?: emptyList() }
        wasValid = cogValid.map { it?.isValid == true }
        thoughts = entry.mapValue { it.thoughts }
        instead = ratRep.mapValue { it.instead }
        actualEmotions = entry.mapValue { emotionText(it.emotion1, it.emotion2, it.emotion3) }
        expressed = entry.mapValue { it.expression }
        relationships = entry.mapValue { it.relationships }
        possibleEmotions = ratRep.mapValue { emotionText(it.emotion1, it.emotion2, it.emotion3) }
        comparison = ratRep.mapValue { it.comparison }

        stage = MediatorLiveData<Stage>().apply {
            val observer = Observer<Any?> {
                val new = when {
                    behavior.value?.complete == true -> Stage.BehaviorComplete
                    behavior.value?.started == true -> Stage.BehaviorPartial
                    ratRep.value?.complete == true -> Stage.RatRepComplete
                    ratRep.value?.started == true -> Stage.RatRepPartial
                    cogValid.value?.complete == true -> Stage.CogValComplete
                    cogValid.value?.started == true -> Stage.CogValPartial
                    entry.value?.complete == true -> Stage.EntryComplete
                    entry.value?.started == true -> Stage.EntryPartial
                    else -> Stage.Begun
                }

                if (this@apply.value != new)
                    this@apply.value = new
            }

            addSource(entry, observer)
            addSource(cogValid, observer)
            addSource(ratRep, observer)
            addSource(behavior, observer)
        }
    }

    private val idValue = MutableLiveData<Int>()

    lateinit var entry: LiveData<Entry?>
    lateinit var cogValid: LiveData<CogValid?>
    lateinit var ratRep: LiveData<RatRep?>
    lateinit var ratRepState: LiveData<Boolean?>
    lateinit var behavior: LiveData<Behavior?>
    lateinit var stage: LiveData<Stage>
    lateinit var situation: LiveData<String?>
    lateinit var typeString: LiveData<String?>
    lateinit var emotionsChosen: LiveData<String?>
    lateinit var thinkingErrors: LiveData<List<String>>
    lateinit var wasValid: LiveData<Boolean>
    lateinit var thoughts: LiveData<String?>
    lateinit var instead: LiveData<String?>
    lateinit var actualEmotions: LiveData<String?>
    lateinit var expressed: LiveData<String?>
    lateinit var comparison: LiveData<String?>
    lateinit var relationships: LiveData<String?>
    lateinit var possibleEmotions: LiveData<String?>

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