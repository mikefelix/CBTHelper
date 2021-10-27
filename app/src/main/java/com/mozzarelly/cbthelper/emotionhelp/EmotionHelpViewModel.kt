package com.mozzarelly.cbthelper.emotionhelp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mozzarelly.cbthelper.CBTDatabase
import com.mozzarelly.cbthelper.Emotion
import com.mozzarelly.cbthelper.InterviewViewModel
import com.mozzarelly.cbthelper.emotionText
import kotlinx.coroutines.launch

class EmotionHelpViewModel : InterviewViewModel() {

    private val ratRepDao by lazy { CBTDatabase.getDatabase(applicationContext).ratRepDao() }

    val desiredEmotion = MutableLiveData<Emotion?>()
    val situation = MutableLiveData<String?>()
    val situationType = MutableLiveData<String?>()
    val assumptions = MutableLiveData<String?>()
    val thinkAndBelieve = MutableLiveData<String?>()

    override val complete: Boolean
        get() = false

    override val title = MutableLiveData("Emotion help")

    override fun save() {
    }

    override fun load(id: Int){
        this.id = id

        viewModelScope.launch {
            (entryDao.get(id) ?: error("Can't find entry $id")).let {
                situation.value = it.situation
                situationType.value = if (it.situationType) "situation" else "conversation"
                assumptions.value = it.assumptions
            }

            changePage(1)
        }

    }
}
