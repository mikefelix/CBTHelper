package com.mozzarelly.cbthelper

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDateTime

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
) : EntryModel {

    companion object {
        fun new(): Entry =
            Entry(
                id = 0,
                date = LocalDateTime.now()
            )

        fun from(model: EntryModel): Entry =
            Entry(
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

interface CogValidModel {
    var id: Int
    var answer1: Int?
    var answer2: Int?
    var answer3: Int?
    var answer4a: String?
    var answer4b: String?
    var answer5: Int?
    var answer6: Int?
    var answer7: Int?
    var answer8: Int?
    var answer9: Int?
    var answer10: Int?
    var answer11: String?
    var answer12: Int?

    fun copyFrom(other: CogValidModel){
        answer1 = other.answer1
        answer2 = other.answer2
        answer3 = other.answer3
        answer4a = other.answer4a
        answer4b = other.answer4b
        answer5 = other.answer5
        answer6 = other.answer6
        answer7 = other.answer7
        answer8 = other.answer8
        answer9 = other.answer9
        answer10 = other.answer10
        answer11 = other.answer11
        answer12 = other.answer12
    }
}

@Entity
data class CogValid(
    @PrimaryKey(autoGenerate = true) override var id: Int,
    @ColumnInfo(name = "answer1") override var answer1:  Int? = null,
    @ColumnInfo(name = "answer2") override var answer2:  Int? = null,
    @ColumnInfo(name = "answer3") override var answer3:  Int? = null,
    @ColumnInfo(name = "answer4a") override var answer4a: String? = null,
    @ColumnInfo(name = "answer4b") override var answer4b: String? = null,
    @ColumnInfo(name = "answer5") override var answer5:  Int? = null,
    @ColumnInfo(name = "answer6") override var answer6:  Int? = null,
    @ColumnInfo(name = "answer7") override var answer7:  Int? = null,
    @ColumnInfo(name = "answer8") override var answer8:  Int? = null,
    @ColumnInfo(name = "answer9") override var answer9:  Int? = null,
    @ColumnInfo(name = "answer10") override var answer10: Int? = null,
    @ColumnInfo(name = "answer11") override var answer11: String? = null,
    @ColumnInfo(name = "answer12") override var answer12: Int? = null
) : CogValidModel {

    companion object {
        fun from(other: CogValidModel): CogValid = CogValid(
            other.id,
            other.answer1,
            other.answer2,
            other.answer3,
            other.answer4a,
            other.answer4b,
            other.answer5,
            other.answer6,
            other.answer7,
            other.answer8,
            other.answer9,
            other.answer10,
            other.answer11,
            other.answer12
        )

        fun new(id: Int): CogValid = CogValid(id)
    }
}