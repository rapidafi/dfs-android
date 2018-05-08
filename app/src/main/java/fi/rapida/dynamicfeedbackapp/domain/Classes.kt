package fi.rapida.dynamicfeedbackapp.domain

import com.google.gson.annotations.SerializedName
import io.upify.utils.domain.APIResult
import io.upify.utils.extensions.timestampMillis

/**
 * Created by egenesio on 20/04/2018.
 */

data class CoursesResponse(
        @SerializedName("meta") val meta: CoursesResponseMeta,
        @SerializedName("data") val courses: List<Course>): APIResult {

    override val isValid: Boolean get() = meta != null

}

data class CourseByCodeResponse(
        @SerializedName("meta") val meta: CoursesResponseMeta,
        @SerializedName("data") val course: Course): APIResult {

    override val isValid: Boolean get() = meta != null

}

data class CoursesResponseMeta(
        @SerializedName("dataCount") val dataCount: Int,
        @SerializedName("courseUnitCode") val courseCode: String?,
        @SerializedName("studentNumber") val studentNumber: String?): APIResult {

    override val isValid: Boolean get() = dataCount != null

}

data class Course(
        @SerializedName("courseUnitCode") val code: String,
        @SerializedName("courseUnitName") val name: String,
        @SerializedName("keyConcepts") val concepts: List<CourseConcept>): APIResult {

    override val isValid: Boolean get() = code != null
}

data class CourseConcept(
        @SerializedName("keyConceptOrder") val order: Int,
        @SerializedName("keyConceptName") val name: String,
        @SerializedName("disposition") val disposition: Disposition): APIResult {

    override val isValid: Boolean get() = order != null && name != null
}

data class Disposition(
        @SerializedName("feeling") private var _feeling: Int,
        @SerializedName("significance") private var _significance: Int,
        @SerializedName("mastery") private var _mastery: Int,
        @SerializedName("comment") var comment: String): APIResult {

    override val isValid: Boolean get() = _feeling != null && _significance != null && _mastery != null

    val feeling: FeelingValue get() = when(_feeling) {
        -1 -> FeelingValue.NEGATIVE
        0 -> FeelingValue.NEUTRAL
        else -> FeelingValue.POSITIVE
    }

    val significance: SignificanceValue get() = when(_significance) {
        -1 -> SignificanceValue.NOT_IMPORTANT
        0 -> SignificanceValue.NEUTRAL
        else -> SignificanceValue.IMPORTANT
    }

    val mastery: MasteryValue get() = when(_mastery) {
        0 -> MasteryValue.NEVER_HEARD
        1 -> MasteryValue.FAMILIAR
        else -> MasteryValue.GOT_IT
    }

    fun setFeeling(value: Int) {
        _feeling = value
    }

    fun setSignificance(value: Int) {
        _significance = value
    }

    fun setMastery(value: Int) {
        _mastery = value
    }
}

enum class FeelingValue {
    NEGATIVE, NEUTRAL, POSITIVE
}

enum class SignificanceValue {
    NOT_IMPORTANT, NEUTRAL, IMPORTANT
}

enum class MasteryValue {
    NEVER_HEARD, FAMILIAR, GOT_IT
}

data class CoursePage(val type: CoursePageType, val concept: CourseConcept? = null, val title: String = concept?.name ?: "") {

    var isDone: Boolean = false

    constructor(concept: CourseConcept) : this(CoursePageType.CONCEPT, concept)
}

enum class CoursePageType {
    INTRO, CONCEPT, SUBMIT
}