package com.mozzarelly.cbthelper.replacement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mozzarelly.cbthelper.*
import com.mozzarelly.cbthelper.editentry.EmotionSelectionViewModel
import kotlinx.coroutines.launch

class ReplacementThoughtsViewModel : InterviewViewModel(), RatRepModel {
    private val cogValidDao by lazy { CBTDatabase.getDatabase(applicationContext).cogValidDao() }
    private val entryDao by lazy { CBTDatabase.getDatabase(applicationContext).entryDao() }
    private val ratRepDao by lazy { CBTDatabase.getDatabase(applicationContext).ratRepDao() }

    val emotionSelection = EmotionSelectionViewModel()

    val thinkingErrors = MutableLiveData<String?>()
    val situation = MutableLiveData<String?>()
    val insteadValue = MutableLiveData<String?>()
    val situationType = MutableLiveData<Boolean>()
    val believeValue = MutableLiveData<Int?>()
    val wouldHaveDoneValue = MutableLiveData<String?>()
    val wouldHaveAffectedValue = MutableLiveData<String?>()

    val thoughts = MutableLiveData<String?>()
    val actualEmotionText = MutableLiveData<String?>()
    val comparisonValue = MutableLiveData<String?>()

    override var id: Int = 0

    override var emotion1: String?
        get() = emotionSelection.emotion1.value
        set(value) { emotionSelection.emotion1.value = value }
    override var emotion2: String?
        get() = emotionSelection.emotion2.value
        set(value) { emotionSelection.emotion2.value = value }
    override var emotion3: String?
        get() = emotionSelection.emotion3.value
        set(value) { emotionSelection.emotion3.value = value }
    override var emotion1Intensity: Int?
        get() = emotionSelection.emotion1Intensity.value
        set(value) { emotionSelection.emotion1Intensity.value = value }
    override var emotion2Intensity: Int?
        get() = emotionSelection.emotion2Intensity.value
        set(value) { emotionSelection.emotion2Intensity.value = value }
    override var emotion3Intensity: Int?
        get() = emotionSelection.emotion3Intensity.value
        set(value) { emotionSelection.emotion3Intensity.value = value }

    override var instead: String?
        get() = insteadValue.value
        set(value) {
            insteadValue.value = value
        }

    override var wouldHaveDone: String?
        get() = wouldHaveDoneValue.value
        set(value) {
            wouldHaveDoneValue.value = value
        }

    override var wouldHaveAffected: String?
        get() = wouldHaveAffectedValue.value
        set(value) {
            wouldHaveAffectedValue.value = value
        }

    override var believe: Int?
        get() = believeValue.value
        set(value) {
            believeValue.value = value
        }

    override var comparison: String?
        get() = comparisonValue.value
        set(value) {
            comparisonValue.value = value
        }

    override val complete: Boolean
        get() = false//TODO

    override val title: LiveData<String?> = page.map {
        "Replacement Thoughts - ${it.first}/$numPages"
    }

    override fun save() {
        viewModelScope.launch {
            ratRepDao.update(RatRep.from(this@ReplacementThoughtsViewModel))
        }
    }

    fun load(id: Int) {
        viewModelScope.launch {
            copyFrom(ratRepDao.get(id) ?: RatRep.new(id).also { ratRepDao.create(it) })

            changePage(when {
                wouldHaveDone != null -> 5
                emotionSelection.emotion1.value != null -> 4
                believe != null -> 3
                instead != null -> 2
                else -> 1
            })
        }

        viewModelScope.launch {
            (entryDao.get(id) ?: error("Can't find entry $id")).let {
                thoughts.value = it.thoughts
                situation.value = it.situation
                situationType.value = it.situationType
                actualEmotionText.value = emotionText(it.emotion1Pair, it.emotion2Pair, it.emotion3Pair)
            }
        }

        viewModelScope.launch {
            (cogValidDao.get(id) ?: error("Can't find cogval $id")).let {
                val advice = it.answer11
                thinkingErrors.value = it.thinkingErrors().joinToString(separator = "\n") + "\n" + advice
            }
        }
    }
}