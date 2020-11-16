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
    var emotion1Intensity: Int?
    var emotion2Intensity: Int?
    var emotion3Intensity: Int?
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
    }

    val emotion1Pair get() = Pair(emotion1, emotion1Intensity)
    val emotion2Pair get() = Pair(emotion2, emotion2Intensity)
    val emotion3Pair get() = Pair(emotion3, emotion3Intensity)

    val emotionString get() = emotionText(emotion1Pair, emotion2Pair, emotion3Pair)
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
    @ColumnInfo(name = "emotion1") override var emotion1: String? = null,
    @ColumnInfo(name = "emotion2") override var emotion2: String? = null,
    @ColumnInfo(name = "emotion3") override var emotion3: String? = null,
    @ColumnInfo(name = "emotion1Intensity") override var emotion1Intensity: Int? = null,
    @ColumnInfo(name = "emotion2Intensity") override var emotion2Intensity: Int? = null,
    @ColumnInfo(name = "emotion3Intensity") override var emotion3Intensity: Int? = null,
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

        fun from(model: EntryModel) = Entry(
            id = model.id,
            situation = model.situation,
            emotion1 = model.emotion1,
            emotion2 = model.emotion2,
            emotion3 = model.emotion3,
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

    val complete: Boolean
        get() = answer12 != null

}

@Entity
data class CogValid(
    @PrimaryKey(autoGenerate = false) override var id: Int = 0,
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
        fun from(other: CogValidModel) = CogValid(
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

    fun thinkingErrors() = listOfNotNull(
        "Emotional reasoning".takeIf { answer1 == 2 },
        "Filtering".takeIf { answer2 in 1..2 },
        "Mislabeling".takeIf { answer3 in 1..2 },
        "Catastrophizing".takeIf { answer5 == 1 },
        "Magnifying".takeIf { answer5 == 2 || answer8 == 2 },
        "Minimizing".takeIf { answer5 == 3 },
        "Jumping to conclusions".takeIf { answer5 == 4 },
        "Confusing possible with probable".takeIf { answer5 == 5 },
        "Overgeneralizing".takeIf { answer6 == 1 },
        "Self projecting".takeIf { answer6 == 2 },
        "Mindreading".takeIf { answer7 == 1 },
        "All-or-nothing thinking".takeIf { answer8 == 1 || answer8 == 2 },
        "Self-blaming (personalizing)".takeIf { answer8 == 3 },
        "Overshoulding".takeIf { answer9 in 1..3 },
        "Confusing wanting and needing".takeIf { answer9 == 4 },
        "Confusing needing and deserving".takeIf { answer9 == 5 }
    )
}

interface RatRepModel {
    var id: Int
    var instead: String?
    var believe: Int?
    var emotion1: String?
    var emotion2: String?
    var emotion3: String?
    var emotion1Intensity: Int?
    var emotion2Intensity: Int?
    var emotion3Intensity: Int?
    var wouldHaveDone: String?
    var wouldHaveAffected: String?
    var comparison: String?

    fun copyFrom(other: RatRepModel){
        instead = other.instead
        believe = other.believe
        emotion1 = other.emotion1
        emotion2 = other.emotion2
        emotion3 = other.emotion3
        emotion1Intensity = other.emotion1Intensity
        emotion2Intensity = other.emotion2Intensity
        emotion3Intensity = other.emotion3Intensity
        wouldHaveDone = other.wouldHaveDone
        wouldHaveAffected = other.wouldHaveAffected
        comparison = other.comparison
    }

    val emotion1Pair get() = Pair(emotion1, emotion1Intensity)
    val emotion2Pair get() = Pair(emotion2, emotion2Intensity)
    val emotion3Pair get() = Pair(emotion3, emotion3Intensity)

    val complete: Boolean
        get() = comparison != null
}

@Entity
data class RatRep(
    @PrimaryKey(autoGenerate = false) override var id: Int = 0,
    @ColumnInfo(name = "instead") override var instead: String? = null,
    @ColumnInfo(name = "believe") override var believe: Int? = null,
    @ColumnInfo(name = "emotion1") override var emotion1: String? = null,
    @ColumnInfo(name = "emotion2") override var emotion2: String? = null,
    @ColumnInfo(name = "emotion3") override var emotion3: String? = null,
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
            emotion1 = other.emotion1,
            emotion2 = other.emotion2,
            emotion3 = other.emotion3,
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

    val complete: Boolean
        get() = rational != null
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
