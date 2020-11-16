@file:Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_OVERRIDE")

package com.mozzarelly.cbthelper.cogvalid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mozzarelly.cbthelper.*
import kotlinx.coroutines.launch

class CogValidViewModel : InterviewViewModel(), CogValidModel {

    companion object {
        var counter = 1
    }

    val cid: Int

    init {
        cid = counter.also { counter += 1 }
        println("Creating CVVM #$cid:")
    }

    override fun toString(): String {
        return "CVVM $cid"
    }

    private val cogValidDao by lazy { CBTDatabase.getDatabase(applicationContext).cogValidDao() }
    private val entryDao by lazy { CBTDatabase.getDatabase(applicationContext).entryDao() }

    override var id: Int = 0

    fun load(id: Int) {
        this.id = id

        viewModelScope.launch {
            (entryDao.get(id) ?: error("Can't find entry $id")).let {
                thoughts.value = it.thoughts
                emotions.value = emotionText(it.emotion1Pair, it.emotion2Pair, it.emotion3Pair)
            }
        }

        viewModelScope.launch {
            val cogValid = cogValidDao.get(id) ?: CogValid.new(id).also { cogValidDao.create(it) }
            copyFrom(cogValid)

            thinkingErrors.value = cogValid.thinkingErrors()

            changePage(when {
                answer12 != null -> 13
                answer11 != null -> 13
                answer10 != null -> 12
                answer9 != null -> 11
                answer8 != null -> 10
                answer7 != null -> 9
                answer6 != null -> 8
                answer5 != null -> 7
                answer4a != null && answer4b != null -> 6
                answer3 != null -> 5
                answer2 != null -> 4
                answer1 != null -> 3
                else -> 1
            })
        }
    }

    override fun save() {
        viewModelScope.launch {
            cogValidDao.update(CogValid.from(this@CogValidViewModel))
        }
    }

    override val complete
        get() = answer12 != null

    val answer1Value = MutableLiveData<Int?>()
    val answer2Value = MutableLiveData<Int?>()
    val answer3Value = MutableLiveData<Int?>()
    val answer4aValue = MutableLiveData<String?>()
    val answer4bValue = MutableLiveData<String?>()
    val answer5Value = MutableLiveData<Int?>()
    val answer6Value = MutableLiveData<Int?>()
    val answer7Value = MutableLiveData<Int?>()
    val answer8Value = MutableLiveData<Int?>()
    val answer9Value = MutableLiveData<Int?>()
    val answer10Value = MutableLiveData<Int?>()
    val answer11Value = MutableLiveData<String?>()
    val answer12Value = MutableLiveData<Int?>()
    val thinkingErrors = MutableLiveData<List<String>>()

    // From entry
    var thoughts = MutableLiveData<String?>()
    var emotions = MutableLiveData<String?>()

    override val title: LiveData<String?> = page.map {
        "Cognition Validity - ${it.first}/$numPages"
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
    override var answer4a: String?
        get() = answer4aValue.value
        set(value) { answer4aValue.value = value }
    override var answer4b: String?
        get() = answer4bValue.value
        set(value) { answer4bValue.value = value }
    override var answer5: Int?
        get() = answer5Value.value
        set(value) { answer5Value.value = value }
    override var answer6: Int?
        get() = answer6Value.value
        set(value) { answer6Value.value = value }
    override var answer7: Int?
        get() = answer7Value.value
        set(value) { answer7Value.value = value }
    override var answer8: Int?
        get() = answer8Value.value
        set(value) { answer8Value.value = value }
    override var answer9: Int?
        get() = answer9Value.value
        set(value) { answer9Value.value = value }
    override var answer10: Int?
        get() = answer10Value.value
        set(value) { answer10Value.value = value }
    override var answer11: String?
        get() = answer11Value.value
        set(value) { answer11Value.value = value }
    override var answer12: Int?
        get() = answer12Value.value
        set(value) { answer12Value.value = value }
}