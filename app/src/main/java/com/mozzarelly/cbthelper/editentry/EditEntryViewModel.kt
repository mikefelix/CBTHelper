@file:Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_OVERRIDE")

package com.mozzarelly.cbthelper.editentry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.mozzarelly.cbthelper.*
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime

class EditEntryViewModel : InterviewViewModel(), EntryModel {

    val dao by lazy { CBTDatabase.getDatabase(applicationContext).entryDao() }

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
            copyFrom(dao.get(id) ?: error("Entry $id not found."))
            changePage(1)
        }
    }


    private suspend fun createAndLoadNew() =
        Entry.new()
            .let { dao.insert(it) }
            .let { dao.get(it.toInt())!! }

    val emotionSelection = EmotionSelectionViewModel()

    val idValue = MutableLiveData<Int>()
    val situationTypeValue = MutableLiveData(false)
    val situationValue = MutableLiveData<String?>()
    val situationDetailValue = MutableLiveData<String?>()

    val completeValue = MutableLiveData(false)
    val dateValue = MutableLiveData<LocalDateTime>()
    val thoughtsValue = MutableLiveData<String?>()
    val expressionValue = MutableLiveData<String?>()
    val bottledValue = MutableLiveData(false)
    val relationshipsValue = MutableLiveData<String?>()
    val assumptionsValue = MutableLiveData<String?>()

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

    override val title: LiveData<String?> = Pair(completeValue, page).map { complete, page ->
        if (complete == true)
            "Completed entry"
        else
            "Add entry" + if (page != null) " - ${page.first}/$numPages" else ""
    }

    override fun save() {
        viewModelScope.launch {
            dao.update(Entry.from(this@EditEntryViewModel))
        }
    }

    inline fun <T, R> LiveData<T>.mapWithAutosave(crossinline convert: (T) -> R): MutableLiveData<R> = MediatorLiveData<R>().apply {
        addSource(this@mapWithAutosave) { t ->
            value = if (t == null) null else convert(t)
        }
    }

}

typealias Emotion = Pair<String?, Int?>

class EmotionSelectionViewModel {
    val emotion1 = MutableLiveData<String?>()
    val emotion2 = MutableLiveData<String?>()
    val emotion3 = MutableLiveData<String?>()
    val emotion1Intensity = MutableLiveData<Int?>()
    val emotion2Intensity = MutableLiveData<Int?>()
    val emotion3Intensity = MutableLiveData<Int?>()

    fun deleteLastEmotion() = when {
        emotion3.value != null -> { emotion3.value = null; emotion3Intensity.value = null }
        emotion2.value != null -> { emotion2.value = null; emotion2Intensity.value = null }
        else -> { emotion1.value = null; emotion1Intensity.value = null }
    }

    val editingEmotion: Pair<MutableLiveData<String?>, MutableLiveData<Int?>>?
        get() = when {
            emotion1.value == null -> Pair(emotion1, emotion1Intensity)
            emotion2.value == null -> Pair(emotion2, emotion2Intensity)
            emotion3.value == null -> Pair(emotion3, emotion3Intensity)
            else -> null
        }

    val emotionsChosen = MediatorLiveData<String?>().apply {
        val observer = Observer<Any?> {
            value = emotionText(Pair(emotion1.value, emotion1Intensity.value),
                Pair(emotion2.value, emotion2Intensity.value),
                Pair(emotion3.value, emotion3Intensity.value))
        }

        addSource(emotion1, observer)
        addSource(emotion2, observer)
        addSource(emotion3, observer)
        addSource(emotion1Intensity, observer)
        addSource(emotion2Intensity, observer)
        addSource(emotion3Intensity, observer)
    }

    val emotionsChosenSimple = MediatorLiveData<String?>().apply {
        val observer = Observer<Any?> {
            value = emotionTextSimple(emotion1.value, emotion2.value, emotion3.value)
        }

        addSource(emotion1, observer)
        addSource(emotion2, observer)
        addSource(emotion3, observer)
    }

    val canAddEmotions = MediatorLiveData<Boolean>().apply {
        addSource(emotion3){
            value = it == null
        }
    }

}
