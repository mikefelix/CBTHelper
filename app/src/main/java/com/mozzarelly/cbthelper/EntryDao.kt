package com.mozzarelly.cbthelper

import androidx.lifecycle.LiveData
import androidx.room.*
import org.threeten.bp.LocalDateTime

@Dao
interface EntryDao {
    @Query("SELECT * FROM entry WHERE id = :id")
    suspend fun get(id: Int): Entry

    @Query("SELECT * FROM entry WHERE id = :id")
    fun getAsync(id: Int): LiveData<Entry>

    @Insert(entity = Entry::class)
    suspend fun insert(entry: Entry): Long

    @Update
    suspend fun update(entry: Entry)

    @Query("SELECT * FROM entry WHERE complete = 1 ORDER BY date DESC")
    fun getAllComplete(): LiveData<List<Entry>>

    @Query("SELECT * FROM entry WHERE complete = 0 ORDER BY date DESC LIMIT 1")
    fun getIncomplete(): LiveData<Entry?>

    @Query("SELECT * FROM entry WHERE complete = 0 ORDER BY date DESC LIMIT 1")
    suspend fun getIncompleteAsync(): Entry?

    @Query("DELETE FROM entry WHERE complete = 0")
    suspend fun deleteIncomplete()

    @Query("DELETE FROM entry WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM entry")
    suspend fun deleteAll()
}

interface EntryModel {
    var id: Int
    var complete: Boolean
    var situationType: Boolean
    var situationDetail: String?
    var situation: String?
    var date: LocalDateTime
    var emotion1: String?
    var emotion2: String?
    var emotion3: String?
    var thoughts: String?
    var expression: String?
    var relationships: String?
    var assumptions: String?
    var bottled: Boolean

    fun copyFrom(model: EntryModel) {
        id = model.id
        situation = model.situation
        emotion1 = model.emotion1
        emotion2 = model.emotion2
        emotion3 = model.emotion3
        complete = model.complete
        date = model.date
        situationType = model.situationType
        situationDetail = model.situationDetail
        thoughts = model.thoughts
        assumptions = model.assumptions
        expression = model.expression
        relationships = model.relationships
        bottled = model.bottled
    }
}

@Entity
data class Entry(
    @PrimaryKey(autoGenerate = true) override var id: Int,
    @ColumnInfo(name = "complete") override var complete: Boolean = false,
    @ColumnInfo(name = "situationType") override var situationType: Boolean = true,
    @ColumnInfo(name = "situationDetail") override var situationDetail: String? = null,
    @ColumnInfo(name = "situation") override var situation: String? = null,
    @ColumnInfo(name = "date") override var date: LocalDateTime,
    @ColumnInfo(name = "emotion1") override var emotion1: String? = null,
    @ColumnInfo(name = "emotion2") override var emotion2: String? = null,
    @ColumnInfo(name = "emotion3") override var emotion3: String? = null,
    @ColumnInfo(name = "thoughts") override var thoughts: String? = null,
    @ColumnInfo(name = "expression") override var expression: String? = null,
    @ColumnInfo(name = "relationships") override var relationships: String? = null,
    @ColumnInfo(name = "assumptions") override var assumptions: String? = null,
    @ColumnInfo(name = "bottled") override var bottled: Boolean = false
) : EntryModel{

    companion object {
        fun new(): Entry = Entry(
            id = 0,
            date = LocalDateTime.now()
        )

        fun from(model: EntryModel): Entry = Entry(
            id = model.id,
            situation = model.situation,
            emotion1 = model.emotion1,
            emotion2 = model.emotion2,
            emotion3 = model.emotion3,
            complete = model.complete,
            date = model.date,
            situationType = model.situationType,
            situationDetail = model.situationDetail,
            thoughts = model.thoughts,
            relationships = model.relationships,
            assumptions = model.assumptions,
            expression = model.expression,
            bottled = model.bottled
        )
    }
}
