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
    var emotion1Name: String?
    var emotion2Name: String?
    var emotion3Name: String?
    var emotion1Intensity: Int?
    var emotion2Intensity: Int?
    var emotion3Intensity: Int?
    var thoughts: String?
    var expression: String?
    var relationships: String?
    var assumptions: String?
    var bottled: Boolean
    var marked: Boolean

    fun copyFrom(model: EntryModel) {
        id = model.id
        situation = model.situation
        emotion1Name = model.emotion1Name
        emotion2Name = model.emotion2Name
        emotion3Name = model.emotion3Name
        emotion1Intensity = model.emotion1Intensity
        emotion2Intensity = model.emotion2Intensity
        emotion3Intensity = model.emotion3Intensity
        complete = model.complete
        date = model.date
        situationType = model.situationType
        situationDetail = model.situationDetail
        thoughts = model.thoughts
        assumptions = model.assumptions
        expression = model.expression
        relationships = model.relationships
        bottled = model.bottled
        marked = model.marked
    }

    val emotion1 get() = emotion1Name?.let { Emotion(it, emotion1Intensity ?: 6) }
    val emotion2 get() = emotion2Name?.let { Emotion(it, emotion2Intensity ?: 6) }
    val emotion3 get() = emotion3Name?.let { Emotion(it, emotion3Intensity ?: 6) }

    val emotionString get() = emotionText(emotion1, emotion2, emotion3)

    val started: Boolean
        get() = situation != null
}

@Entity
data class Entry(
    @PrimaryKey(autoGenerate = true) override var id: Int = 0,
    @ColumnInfo(name = "complete") override var complete: Boolean = false,
    @ColumnInfo(name = "deleted") var deleted: Boolean = false,
    @ColumnInfo(name = "situationType") override var situationType: Boolean = true,
    @ColumnInfo(name = "situationDetail") override var situationDetail: String? = null,
    @ColumnInfo(name = "situation") override var situation: String? = null,
    @ColumnInfo(name = "date") override var date: LocalDateTime,
    @ColumnInfo(name = "emotion1") override var emotion1Name: String? = null,
    @ColumnInfo(name = "emotion2") override var emotion2Name: String? = null,
    @ColumnInfo(name = "emotion3") override var emotion3Name: String? = null,
    @ColumnInfo(name = "emotion1Intensity") override var emotion1Intensity: Int? = null,
    @ColumnInfo(name = "emotion2Intensity") override var emotion2Intensity: Int? = null,
    @ColumnInfo(name = "emotion3Intensity") override var emotion3Intensity: Int? = null,
    @ColumnInfo(name = "thoughts") override var thoughts: String? = null,
    @ColumnInfo(name = "expression") override var expression: String? = null,
    @ColumnInfo(name = "relationships") override var relationships: String? = null,
    @ColumnInfo(name = "assumptions") override var assumptions: String? = null,
    @ColumnInfo(name = "bottled") override var bottled: Boolean = false,
    @ColumnInfo(name = "marked") override var marked: Boolean = false
) : EntryModel {

    companion object {
        fun new(): Entry =
            Entry(
                id = 0,
                date = LocalDateTime.now()
            )

        fun from(model: EntryModel) = Entry(
            id = model.id,
            situation = model.situation,
            emotion1Name = model.emotion1Name,
            emotion2Name = model.emotion2Name,
            emotion3Name = model.emotion3Name,
            emotion1Intensity = model.emotion1Intensity,
            emotion2Intensity = model.emotion2Intensity,
            emotion3Intensity = model.emotion3Intensity,
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
    var answer4: Int?
    var answer5: Int?
    var answer6: Int?
    var answer7: Int?
    var answer8: String?
    var answer9: Int?
    var answer10: Int?

    fun copyFrom(other: CogValidModel){
        answer1 = other.answer1
        answer2 = other.answer2
        answer3 = other.answer3
        answer4 = other.answer4
        answer5 = other.answer5
        answer6 = other.answer6
        answer7 = other.answer7
        answer8 = other.answer8
        answer9 = other.answer9
        answer10 = other.answer10
    }

    val complete: Boolean
        get() = answer10 != null

    val started: Boolean
        get() = answer1 != null

}

@Entity
data class CogValid(
    @PrimaryKey(autoGenerate = false) override var id: Int = 0,
    @ColumnInfo(name = "answer1") override var answer1:  Int? = null,
    @ColumnInfo(name = "answer2") override var answer2:  Int? = null,
    @ColumnInfo(name = "answer3") override var answer3:  Int? = null,
    @ColumnInfo(name = "answer4a") override var answer4: Int? = null,
    @ColumnInfo(name = "answer5") override var answer5:  Int? = null,
    @ColumnInfo(name = "answer6") override var answer6:  Int? = null,
    @ColumnInfo(name = "answer7") override var answer7:  Int? = null,
    @ColumnInfo(name = "answer8") override var answer8:  String? = null,
    @ColumnInfo(name = "answer9") override var answer9:  Int? = null,
    @ColumnInfo(name = "answer10") override var answer10: Int? = null
) : CogValidModel {

    val isValid: Boolean
        get() = thinkingErrors().isEmpty()

    companion object {
        fun from(other: CogValidModel) = CogValid(
            other.id,
            other.answer1,
            other.answer2,
            other.answer3,
            other.answer4,
            other.answer5,
            other.answer6,
            other.answer7,
            other.answer8,
            other.answer9,
            other.answer10
        )

        fun new(id: Int): CogValid = CogValid(id)
    }

    fun thinkingErrors() = listOfNotNull(
        "Mislabeling".takeIf { answer1 in 1..2 },
        "Emotional reasoning".takeIf { answer2 == 2 },
        "Catastrophizing".takeIf { answer3 == 1 },
        "Magnifying".takeIf { answer3 == 2 || answer6 == 2 },
        "Minimizing".takeIf { answer3 == 3 },
        "Jumping to conclusions".takeIf { answer3 == 4 },
        "Overgeneralizing".takeIf { answer3 == 5 },
        "Confusing possible with probable".takeIf { answer3 == 6 },
        "Mindreading".takeIf { answer4 == 1 },
        "Filtering".takeIf { answer5 == 1 },
        "Self projecting".takeIf { answer5 == 2 },
        "All-or-nothing thinking".takeIf { answer6 == 1 || answer6 == 2 },
        "Other-blaming".takeIf { answer6 == 3 },
        "Self-blaming (personalizing)".takeIf { answer6 == 4 },
        "Overshoulding".takeIf { answer7 in 1..3 },
        "Confusing wanting and needing".takeIf { answer7 == 4 },
        "Confusing needing and deserving".takeIf { answer7 == 5 },
        "Cognitive dissonance".takeIf { answer9 == 2 }
    )
}

interface RatRepModel {
    var id: Int
    var instead: String?
    var believe: Int?
    var emotion1Name: String?
    var emotion2Name: String?
    var emotion3Name: String?
    var emotion1Intensity: Int?
    var emotion2Intensity: Int?
    var emotion3Intensity: Int?
    var wouldHaveDone: String?
    var wouldHaveAffected: String?
    var comparison: String?

    fun copyFrom(other: RatRepModel){
        instead = other.instead
        believe = other.believe
        emotion1Name = other.emotion1Name
        emotion2Name = other.emotion2Name
        emotion3Name = other.emotion3Name
        emotion1Intensity = other.emotion1Intensity
        emotion2Intensity = other.emotion2Intensity
        emotion3Intensity = other.emotion3Intensity
        wouldHaveDone = other.wouldHaveDone
        wouldHaveAffected = other.wouldHaveAffected
        comparison = other.comparison
    }

    val emotion1 get() = emotion1Name?.let { Emotion(it, emotion1Intensity ?: 6) }
    val emotion2 get() = emotion2Name?.let { Emotion(it, emotion2Intensity ?: 6) }
    val emotion3 get() = emotion3Name?.let { Emotion(it, emotion3Intensity ?: 6) }

    val complete: Boolean
        get() = comparison != null

    val started: Boolean
        get() = instead != null
}

@Entity
data class RatRep(
    @PrimaryKey(autoGenerate = false) override var id: Int = 0,
    @ColumnInfo(name = "instead") override var instead: String? = null,
    @ColumnInfo(name = "believe") override var believe: Int? = null,
    @ColumnInfo(name = "emotion1") override var emotion1Name: String? = null,
    @ColumnInfo(name = "emotion2") override var emotion2Name: String? = null,
    @ColumnInfo(name = "emotion3") override var emotion3Name: String? = null,
    @ColumnInfo(name = "emotion1Intensity") override var emotion1Intensity: Int? = null,
    @ColumnInfo(name = "emotion2Intensity") override var emotion2Intensity: Int? = null,
    @ColumnInfo(name = "emotion3Intensity") override var emotion3Intensity: Int? = null,
    @ColumnInfo(name = "wouldHaveDone") override var wouldHaveDone: String? = null,
    @ColumnInfo(name = "wouldHaveAffected") override var wouldHaveAffected: String? = null,
    @ColumnInfo(name = "comparison") override var comparison: String? = null
) : RatRepModel {

    companion object {
        fun from(other: RatRepModel) = RatRep(
            id = other.id,
            instead = other.instead,
            believe = other.believe,
            emotion1Name = other.emotion1Name,
            emotion2Name = other.emotion2Name,
            emotion3Name = other.emotion3Name,
            emotion1Intensity = other.emotion1Intensity,
            emotion2Intensity = other.emotion2Intensity,
            emotion3Intensity = other.emotion3Intensity,
            wouldHaveDone = other.wouldHaveDone,
            wouldHaveAffected = other.wouldHaveAffected,
            comparison = other.comparison
        )

        fun new(id: Int): RatRep = RatRep(id)
    }

}

interface BehaviorModel {
    var id: Int
    var honest: Int?
    var person: String?
    var disappointed: Int?
    var disapprove: Int?
    var embarrassed: Int?
    var relationships: Int?
    var occupations: Int?
    var health: Int?
    var other: Int?
    var rational: Int?

    fun copyFrom(other: Behavior){
        honest = other.honest
        person = other.person
        disappointed = other.disappointed
        disapprove = other.disapprove
        embarrassed = other.embarrassed
        relationships = other.relationships
        occupations = other.occupations
        health = other.health
        this.other = other.other
        rational = other.rational
    }

    val complete: Boolean
        get() = rational != null

    val started: Boolean
        get() = person != null
}

@Entity
data class Behavior(
    @PrimaryKey(autoGenerate = false) override var id: Int = 0,
    @ColumnInfo(name = "honest") override var honest: Int? = null,
    @ColumnInfo(name = "person") override var person: String? = null,
    @ColumnInfo(name = "disappointed") override var disappointed: Int? = null,
    @ColumnInfo(name = "disapprove") override var disapprove: Int? = null,
    @ColumnInfo(name = "embarrassed") override var embarrassed: Int? = null,
    @ColumnInfo(name = "relationships") override var relationships: Int? = null,
    @ColumnInfo(name = "occupations") override var occupations: Int? = null,
    @ColumnInfo(name = "health") override var health: Int? = null,
    @ColumnInfo(name = "other") override var other: Int? = null,
    @ColumnInfo(name = "rational") override var rational: Int? = null
) : BehaviorModel {
    companion object {
        fun from(other: BehaviorModel) = Behavior(
            other.id,
            other.honest,
            other.person,
            other.disappointed,
            other.disapprove,
            other.embarrassed,
            other.relationships,
            other.occupations,
            other.health,
            other.other,
            other.rational
        )

        fun new(id: Int): Behavior = Behavior(id)
    }
}

data class Emotion(val emotion: String, val intensity: Int){
    override fun toString(): String = "$emotion ($intensity/10)"

    val valid = emotion.isNotBlank()
}
