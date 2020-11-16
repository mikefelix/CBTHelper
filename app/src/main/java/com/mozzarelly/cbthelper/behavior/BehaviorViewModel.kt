@file:Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_OVERRIDE")

package com.mozzarelly.cbthelper.behavior

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mozzarelly.cbthelper.*
import kotlinx.coroutines.launch

class BehaviorViewModel : InterviewViewModel(), BehaviorModel {

    private val behaviorDao by lazy { CBTDatabase.getDatabase(applicationContext).behaviorDao() }
    private val entryDao by lazy { CBTDatabase.getDatabase(applicationContext).entryDao() }

    override val title: LiveData<String?> = page.map {
        "Behavior - ${it.first}/$numPages"
    }

    override var id: Int = 0
    override var honest: Int?
        get() = honestValue.value
        set(value){
            honestValue.value = value
        }
    override var person: String?
        get() = personValue.value
        set(value){
            personValue.value = value
        }
    override var disappointed: Int?
        get() = disappointedValue.value
        set(value){
            disappointedValue.value = value
        }
    override var disapprove: Int?
        get() = disapproveValue.value
        set(value){
            disapproveValue.value = value
        }
    override var embarrassed: Int?
        get() = embarrassedValue.value
        set(value){
            embarrassedValue.value = value
        }
    override var relationships: Int?
        get() = relationshipsValue.value
        set(value) {
            relationshipsValue.value = value
        }
    override var occupations: Int?
        get() = occupationsValue.value
        set(value) {
            occupationsValue.value = value
        }
    override var health: Int?
        get() = healthValue.value
        set(value) {
            healthValue.value = value
        }
    override var other: Int?
        get() = otherValue.value
        set(value) {
            otherValue.value = value
        }
    override var rational: Int?
        get() = rationalValue.value
        set(value) {
            rationalValue.value = value
        }

    fun load(id: Int) {
        this.id = id

        viewModelScope.launch {
            entry.value = entryDao.get(id) ?: error("Can't find entry $id")
        }

        viewModelScope.launch {
            (behaviorDao.get(id) ?: Behavior.new(id).also { behaviorDao.create(it) }).let {
                honest = it.honest
                person = it.person
                disappointed = it.disappointed
                disapprove = it.disapprove
                embarrassed = it.embarrassed
                relationships = it.relationships
                occupations = it.occupations
                health = it.health
                other = it.other
                rational = it.rational
            }

            changePage(when {
                else -> 1
            })
        }
    }

    override fun save() {
        viewModelScope.launch {
            behaviorDao.update(Behavior.from(this@BehaviorViewModel))
        }
    }

    override val complete
        get() = rational != null

    val entry = MutableLiveData<Entry?>()

    val emotions = entry.mapValueAs { emotionString }
    val expression = entry.mapValueAs { expression }

    val honestValue = MutableLiveData<Int?>()
    val personValue = MutableLiveData<String?>()
    val disappointedValue = MutableLiveData<Int?>()
    val disapproveValue = MutableLiveData<Int?>()
    val embarrassedValue = MutableLiveData<Int?>()
    val relationshipsValue = MutableLiveData<Int?>()
    val occupationsValue = MutableLiveData<Int?>()
    val healthValue = MutableLiveData<Int?>()
    val otherValue = MutableLiveData<Int?>()
    val rationalValue = MutableLiveData<Int?>()
}