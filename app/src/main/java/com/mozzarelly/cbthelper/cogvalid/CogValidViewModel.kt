@file:Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_OVERRIDE")

package com.mozzarelly.cbthelper.cogvalid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mozzarelly.cbthelper.*
import kotlinx.coroutines.launch

class CogValidViewModel : InterviewViewModel(), CogValidModel {

    private val cogValidDao by lazy { CBTDatabase.getDatabase().cogValidDao() }

    override fun load(id: Int) {
        this.id = id

        viewModelScope.launch {
            (entryDao.get(id) ?: error("Can't find entry $id")).let {
                thoughts.value = it.thoughts
                emotions.value = emotionText(it.emotion1, it.emotion2, it.emotion3)
            }
        }

        viewModelScope.launch {
            val cogValid = cogValidDao.get(id) ?: CogValid.new(id).also { cogValidDao.create(it) }
            copyFrom(cogValid)

            changePage(when {
                answer10 != null -> 2
                answer9 != null -> 12
                answer8 != null -> 11
                answer7 != null -> 10
                answer6 != null -> 9
                answer5 != null -> 8
                answer4 != null -> 7
                answer3 != null -> 6
                answer2 != null -> 5
                answer1 != null -> 4
                else -> 1
            })
        }
    }

    fun skip(rational: Boolean){
        skippedBecause = rational
/*        answer1 = 3
        answer2 = 1
        answer3 = 7
        answer4 = 3
        answer5 = 3
        answer6 = 5
        answer7 = 6
        answer8 = "someone you consider rational"
        answer9 = 1
        answer10 = 1*/
        save()
    }

    override suspend fun saveAsync(): SaveResult {
        cogValidDao.update(CogValid.from(this@CogValidViewModel))
        return if (complete) SaveResult.SavedComplete(id) else SaveResult.SavedPartial(id)
    }

    override val complete
        get() = skippedBecause != null || answer10 != null

    val skippedBecauseValue = MutableLiveData<Boolean?>()
    val answer1Value = MutableLiveData<Int?>()
    val answer2Value = MutableLiveData<Int?>()
    val answer3Value = MutableLiveData<Int?>()
    val answer4Value = MutableLiveData<Int?>()
    val answer5Value = MutableLiveData<Int?>()
    val answer6Value = MutableLiveData<Int?>()
    val answer7Value = MutableLiveData<Int?>()
    val answer8Value = MutableLiveData<String?>()
    val answer9Value = MutableLiveData<Int?>()
    val answer10Value = MutableLiveData<Int?>()

    // From entry
    var thoughts = MutableLiveData<String?>()
    var emotions = MutableLiveData<String?>()

    override val title: LiveData<String?> = page.map {
        when (it.first) {
            1 -> "Cognition Validity"
            else -> "Cognition Validity - ${it.first - 1}/${numPages - 1}"
        }
    }

    override var answer1: Int?
        get() = answer1Value.value
        set(value) { answer1Value.value = value }
    override var answer2: Int?
        get() = answer2Value.value
        set(value) { answer2Value.value = value }
    override var answer3: Int?
        get() = answer3Value.value
        set(value) { answer3Value.value = value }
    override var answer4: Int?
        get() = answer4Value.value
        set(value) { answer4Value.value = value }
    override var answer5: Int?
        get() = answer5Value.value
        set(value) { answer5Value.value = value }
    override var answer6: Int?
        get() = answer6Value.value
        set(value) { answer6Value.value = value }
    override var answer7: Int?
        get() = answer7Value.value
        set(value) { answer7Value.value = value }
    override var answer8: String?
        get() = answer8Value.value
        set(value) { answer8Value.value = value }
    override var answer9: Int?
        get() = answer9Value.value
        set(value) { answer9Value.value = value }
    override var answer10: Int?
        get() = answer10Value.value
        set(value) { answer10Value.value = value }
    override var skippedBecause: Boolean?
        get() = skippedBecauseValue.value
        set(value) { skippedBecauseValue.value = value }

    override val patientGuidePage = when {
        currPage <= 1 -> PatientGuide.Page.ThreeA
        else -> PatientGuide.Page.ThreeB
    }

}