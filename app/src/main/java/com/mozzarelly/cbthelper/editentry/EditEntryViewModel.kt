@file:Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_OVERRIDE")

package com.mozzarelly.cbthelper.editentry

import androidx.lifecycle.*
import com.mozzarelly.cbthelper.*
import com.mozzarelly.cbthelper.SaveResult.*
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime

class EditEntryViewModel : InterviewViewModel(), EntryModel {

    val dao by lazy { CBTDatabase.getDatabase().entryDao() }

    // Not called
    override fun load(id: Int) {
        this.id = id
        loadEntry(id)
    }

    fun loadNewEntry() {
        viewModelScope.launch {
            dao.deleteIncomplete()
            copyFrom(createAndLoadNew())
            changePage(1)
        }
    }

    fun loadEntryInProgress(){
        viewModelScope.launch {
            val entry = dao.getIncomplete() ?: createAndLoadNew()
            copyFrom(entry)
            changePage(when {
                situation.isNullOrBlank() -> 1
                emotion1Name.isNullOrBlank() -> 2
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
            copyFrom(dao.get(id) ?: error("Entry $id not found."))
            changePage(1)
        }
    }


    private suspend fun createAndLoadNew() =
        Entry.new()
            .let { dao.insert(it) }
            .let { dao.get(it.toInt())!! }

//    val emotionSelection = EmotionSelectionViewModel()

    val idValue = MutableLiveData<Int>()
    val situationTypeValue = MutableLiveData(false)
    val situationValue = MutableLiveData<String?>()
    val whoWhereValue = MutableLiveData<String?>()

    val completeValue = MutableLiveData(false)
    val dateValue = MutableLiveData<LocalDateTime>()
    val thoughtsValue = MutableLiveData<String?>()
    val expressionValue = MutableLiveData<String?>()
    val bottledValue = MutableLiveData(false)
    val relationshipsValue = MutableLiveData<String?>()
    val assumptionsValue = MutableLiveData<String?>()
    val markedValue = MutableLiveData<Boolean>()
    
    val emotion1Value = MutableLiveData<Emotion?>()
    val emotion2Value = MutableLiveData<Emotion?>()
    val emotion3Value = MutableLiveData<Emotion?>()

    val emotionsChosenSimple = Triple(emotion1Value, emotion2Value, emotion3Value).map { e1, e2, e3 ->
        listOf(e1, e2, e3).toSimpleText()
    }

    override var complete: Boolean
        get() = completeValue.valueNotNull()
        set(value) { completeValue.value = value }
    override var situationType: Boolean
        get() = situationTypeValue.valueNotNull()
        set(value) { situationTypeValue.value = value }
    override var situation: String?
        get() = situationValue.value
        set(value) { situationValue.value = value }
    override var whoWhere: String?
        get() = whoWhereValue.value
        set(value) { whoWhereValue.value = value }
    override var date: LocalDateTime
        get() = dateValue.valueNotNull()
        set(value) { dateValue.value = value }
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
    override var marked: Boolean
        get() = markedValue.valueNotNull()
        set(value) { markedValue.value = value }

    override val title: LiveData<String?> = Pair(completeValue, page).map { complete, page ->
        if (complete == true)
            "Completed entry"
        else
            page?.first?.takeIf { it > 0 }?.let { "Add entry - ${it}/$numPages" } ?: "Add entry"
    }

    override suspend fun saveAsync(): SaveResult {
        return try {
            when  {
                situation.isNullOrBlank() -> NoChange
                complete -> {
                    dao.update(Entry.from(this@EditEntryViewModel))
                    SavedComplete(id)
                }
                else -> {
                    dao.update(Entry.from(this@EditEntryViewModel))
                    SavedPartial(id)
                }
            }
        }
        catch (e: Exception) {
            e.rethrowIfCancellation()
            Error(e)
        }
    }

    inline fun <T, R> LiveData<T>.mapWithAutosave(crossinline convert: (T) -> R): MutableLiveData<R> = MediatorLiveData<R>().apply {
        addSource(this@mapWithAutosave) { t ->
            value = if (t == null) null else convert(t)
        }
    }

}


