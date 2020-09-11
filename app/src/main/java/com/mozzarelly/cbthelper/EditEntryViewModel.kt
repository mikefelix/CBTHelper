@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.mozzarelly.cbthelper

import androidx.lifecycle.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime

class EditEntryViewModel(private val numPages: Int, private val dao: EntryDao) : ViewModel(), EntryModel {

    fun loadNewEntry() {
        viewModelScope.launch {
            dao.deleteIncomplete()
            copyFrom(createAndLoadNew())
        }
    }

    fun loadEntryInProgress(){
        viewModelScope.launch {
            val entry = dao.getIncompleteAsync() ?: createAndLoadNew()
            copyFrom(entry)
            changePage(when {
                situation.isNullOrBlank() -> 1
                emotion1.isNullOrBlank() -> 2
                thoughts.isNullOrBlank() -> 3
                expression.isNullOrBlank() -> 4
                relationships.isNullOrBlank() -> 5
                assumptions.isNullOrBlank() -> 6
                else -> 1
            })
        }
    }

    fun loadEntry(id: Int) {
        viewModelScope.launch {
            copyFrom(dao.get(id))
        }
    }

    fun deleteLastEmotion() = when {
        emotion3 != null -> emotion3 = null
        emotion2 != null -> emotion2 = null
        else -> emotion1 = null
    }

    private suspend fun createAndLoadNew() =
        Entry.new()
            .let { dao.insert(it) }
            .let { dao.get(it.toInt()) }

    val editingEmotion: MutableLiveData<String?>?
        get() = when {
            emotion1 == null -> emotion1Value
            emotion2 == null -> emotion2Value
            emotion3 == null -> emotion3Value
            else -> null
        }

    val idValue = MutableLiveData<Int>()
    val situationTypeValue = MutableLiveData<Boolean>(false)
    val situationValue = MutableLiveData<String?>()
    val situationDetailValue = MutableLiveData<String?>()

    val completeValue = MutableLiveData(false)
    val dateValue = MutableLiveData<LocalDateTime>()
    val emotion1Value = MutableLiveData<String?>()
    val emotion2Value = MutableLiveData<String?>()
    val emotion3Value = MutableLiveData<String?>()
    val thoughtsValue = MutableLiveData<String?>()
    val expressionValue = MutableLiveData<String?>()
    val bottledValue = MutableLiveData(false)
    val relationshipsValue = MutableLiveData<String?>()
    val assumptionsValue = MutableLiveData<String?>()

    val emotionsChosenValue = Triple(emotion1Value, emotion2Value, emotion3Value).map { e1, e2, e3 -> emotionText(e1, e2, e3) }

//    val canAddEmotions = emotion3Value.map { it == null }
    val canAddEmotions = MediatorLiveData<Boolean>().apply {
        addSource(emotion3Value){
            value = it == null
        }
    }

    var page = MutableLiveData<Pair<Int, Int?>>(Pair(1, null))

    private val changingPageChannel = BroadcastChannel<Pair<Int, Int?>>(Channel.BUFFERED)
    val changingPage = changingPageChannel.asFlow()

    val selectedEmotionGroupIndex = MutableLiveData(0)
    val selectedEmotionIndex = MutableLiveData(0).apply {
        observeForever {
            val emotion = it.takeIf { it > 0 }?.let { Emotions.emotions[selectedEmotionGroupIndex.valueNotNull()][it] }
            val liveData = when {
                emotion3Value.value != null -> null
                emotion2Value.value != null -> emotion3Value
                emotion1Value.value != null -> emotion2Value
                else -> emotion1Value
            }

            if (liveData != null && emotion != liveData.value){
                liveData.value = emotion
            }
        }
    }

    override var id: Int
        get() = idValue.valueNotNull()
        set(value) { idValue.value = value }
    override var complete: Boolean
        get() = completeValue.valueNotNull()
        set(value) { completeValue.value = value }
    override var situationType: Boolean
        get() = situationTypeValue.valueNotNull()
        set(value) { situationTypeValue.value = value }
    override var situation: String?
        get() = situationValue.value
        set(value) { situationValue.value = value }
    override var situationDetail: String?
        get() = situationDetailValue.value
        set(value) { situationDetailValue.value = value }
    override var date: LocalDateTime
        get() = dateValue.valueNotNull()
        set(value) { dateValue.value = value }
    override var emotion1: String?
        get() = emotion1Value.value
        set(value) { emotion1Value.value = value }
    override var emotion2: String?
        get() = emotion2Value.value
        set(value) { emotion2Value.value = value }
    override var emotion3: String?
        get() = emotion3Value.value
        set(value) { emotion3Value.value = value }
    override var thoughts: String?
        get() = thoughtsValue.value
        set(value) { thoughtsValue.value = value }
    override var expression: String?
        get() = expressionValue.value
        set(value) { expressionValue.value = value }
    override var relationships: String?
        get() = relationshipsValue.value
        set(value) { relationshipsValue.value = value }
    override var assumptions: String?
        get() = assumptionsValue.value
        set(value) { assumptionsValue.value = value }
    override var bottled: Boolean
        get() = bottledValue.valueNotNull()
        set(value) { bottledValue.value = value }

    private fun <T> LiveData<T>.valueNotNull() = value!!

    fun save() {
        viewModelScope.launch {
            dao.update(Entry.from(this@EditEntryViewModel))
        }
    }

    private val currPage: Int
        get() = page.value?.first ?: 1

    fun previousPage(){
        changePage(currPage - 1)
        save()
    }

    fun nextPage(){
        changePage(currPage + 1)
        save()
    }

    private fun changePage(newPage: Int){
        val oldPage = currPage
        val anim = newPage.compareTo(currPage)

        Pair(newPage.coerceAtLeast(1).coerceAtMost(numPages), anim).let {
            page.value = it

            if (it.first != oldPage)
                changingPageChannel.offer(it)
        }
    }

    inline fun <T, R> LiveData<T>.mapWithAutosave(crossinline convert: (T) -> R): MutableLiveData<R> = MediatorLiveData<R>().apply {
        addSource(this@mapWithAutosave) { t ->
            value = if (t == null) null else convert(t)
        }
    }

}