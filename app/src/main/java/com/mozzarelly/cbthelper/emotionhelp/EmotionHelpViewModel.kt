package com.mozzarelly.cbthelper.emotionhelp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mozzarelly.cbthelper.*
import com.mozzarelly.cbthelper.SaveResult.NoChange
import kotlinx.coroutines.launch

class EmotionHelpViewModel : InterviewViewModel() {

    private val ratRepDao by lazy { CBTDatabase.getDatabase().ratRepDao() }

    val desiredEmotion = MutableLiveData<Emotion?>()
    val situation = MutableLiveData<String?>()
    val situationType = MutableLiveData<String?>()
    val assumptions = MutableLiveData<String?>()
    val thinkAndBelieve = MutableLiveData<String?>()

    override val complete: Boolean
        get() = false

    override val title = MutableLiveData("Emotion help")

    override suspend fun saveAsync(): SaveResult = NoChange

    override fun load(id: Int){
        this.id = id

        viewModelScope.launch {
            (entryDao.get(id) ?: error("Can't find entry $id")).let {
                situation.value = it.situation
                situationType.value = it.situationTypeText
                assumptions.value = it.assumptions
            }

            changePage(1)
        }

    }
}
