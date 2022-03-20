package com.mozzarelly.cbthelper.replacement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mozzarelly.cbthelper.*
import kotlinx.coroutines.launch

class ReplacementThoughtsViewModel : InterviewViewModel(), RatRepModel {
    private val cogValidDao by lazy { CBTDatabase.getDatabase().cogValidDao() }
    private val ratRepDao by lazy { CBTDatabase.getDatabase().ratRepDao() }

    val thinkingErrors = MutableLiveData<String?>()
    val situation = MutableLiveData<String?>()
    val thinkInsteadValue = MutableLiveData<String?>()
    val situationType = MutableLiveData<String>()
    val believeValue = MutableLiveData<Int?>()
    val wouldHaveDoneValue = MutableLiveData<String?>()
    val wouldHaveAffectedValue = MutableLiveData<String?>()
    val emotion1Value = MutableLiveData<Emotion?>()
    val emotion2Value = MutableLiveData<Emotion?>()
    val emotion3Value = MutableLiveData<Emotion?>()

    val insteadFelt = Triple(emotion1Value, emotion2Value, emotion3Value).map { e1, e2, e3 ->
        listOf(e1, e2, e3).toSimpleText()
    }

    override var emotion1Name: String?
        get() = emotion1Value.value?.emotion
        set(value) {
            emotion1Value.value = value?.let {
                Emotion(it, emotion1Intensity ?: 5)
            }
        }
    override var emotion2Name: String?
        get() = emotion2Value.value?.emotion
        set(value) {
            emotion2Value.value = value?.let {
                Emotion(it, emotion2Intensity ?: 5)
            }
        }
    override var emotion3Name: String?
        get() = emotion3Value.value?.emotion
        set(value) {
            emotion3Value.value = value?.let {
                Emotion(it, emotion3Intensity ?: 5)
            }
        }
    override var emotion1Intensity: Int?
        get() = emotion1Value.value?.intensity
        set(value) {
            emotion1Value.value = value?.let {
                Emotion(emotion1Name ?: "unknown", emotion1Intensity ?: 5)
            }
        }
    override var emotion2Intensity: Int?
        get() = emotion2Value.value?.intensity
        set(value) {
            emotion2Value.value = value?.let {
                Emotion(emotion2Name ?: "unknown", emotion2Intensity ?: 5)
            }
        }
    override var emotion3Intensity: Int?
        get() = emotion3Value.value?.intensity
        set(value) {
            emotion3Value.value = value?.let {
                Emotion(emotion3Name ?: "unknown", emotion3Intensity ?: 5)
            }
        }

    val thoughts = MutableLiveData<String?>()
    val actualEmotionText = MutableLiveData<String?>()
    val comparisonValue = MutableLiveData<String?>()

    override var thinkInstead: String?
        get() = thinkInsteadValue.value
        set(value) {
            thinkInsteadValue.value = value
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
        get() = wouldHaveAffected != null

    override val title: LiveData<String?> = page.map {
        "Replacement Thoughts - ${it.first}/$numPages"
    }

    override suspend fun saveAsync(): SaveResult {
        ratRepDao.update(RatRep.from(this@ReplacementThoughtsViewModel))
        return if (complete) SaveResult.SavedComplete(id) else SaveResult.SavedPartial(id)
    }

    override fun load(id: Int) {
        this.id = id

        viewModelScope.launch {
            copyFrom(ratRepDao.get(id) ?: RatRep.new(id).also { ratRepDao.create(it) })

            changePage(when {
                wouldHaveDone != null -> 5
                emotion1Name != null -> 4
                believe != null -> 3
                thinkInstead != null -> 2
                else -> 1
            })
        }

        viewModelScope.launch {
            (entryDao.get(id) ?: error("Can't find entry $id")).let {
                thoughts.value = it.thoughts
                situation.value = it.situation
                situationType.value = it.situationTypeText
                actualEmotionText.value = emotionText(it.emotion1, it.emotion2, it.emotion3)
            }
        }

        viewModelScope.launch {
            (cogValidDao.get(id) ?: error("Can't find cogval $id")).let {
                thinkingErrors.value = it.errors().joinToString(separator = "\n")// + "\n" + advice
            }
        }
    }

    override val patientGuidePage = when {
        currPage >= 3 -> PatientGuide.Page.FourA
        else -> PatientGuide.Page.FourB1
    }
}