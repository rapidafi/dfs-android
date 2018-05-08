package fi.rapida.dynamicfeedbackapp.session

import android.content.Context
import fi.rapida.dynamicfeedbackapp.R
import fi.rapida.dynamicfeedbackapp.app.App
import fi.rapida.dynamicfeedbackapp.domain.*
import fi.rapida.dynamicfeedbackapp.networking.APIClient
import fi.rapida.dynamicfeedbackapp.networking.APIEndpoint
import io.upify.utils.extensions.DelegatesExt
import io.upify.utils.extensions.first
import kotlin.properties.Delegates

/**
 * Created by egenesio on 20/04/2018.
 */
class SessionManager private constructor() {

    companion object {
        val instance = SessionManager()

        val STUDENT_ID_MAX = 6

        private val FIELD_LAST_STUDENT_ID = "FIELD_LAST_STUDENT_ID"
        private val FIELD_LAST_COURSE_CODE = "FIELD_LAST_COURSE_CODE"
    }

    var studentId: String? by DelegatesExt.preference(FIELD_LAST_STUDENT_ID, null)
    var courseCode: String? by DelegatesExt.preference(FIELD_LAST_COURSE_CODE, null)

    var courses: List<Course>? = null

    var courseSelected: Course? by Delegates.observable<Course?>(null) { _, _, newValue ->

        newValue?.let { selected ->
            val list: MutableList<CoursePage> = mutableListOf()

            list.add(CoursePage(CoursePageType.INTRO, title = App.instance.getString(R.string.page_intro_title)))
            selected.concepts.sortedBy { it.order }.forEach {
                list.add(CoursePage(it))
            }
            list.add(CoursePage(CoursePageType.SUBMIT, title = App.instance.getString(R.string.page_submit_title)))

            coursePages = list
        } ?: run {
            coursePages = listOf()
        }

    }
    private set

    var coursePages: List<CoursePage> = listOf()

    fun fetchCourses(force: Boolean = false, onCompletion: (courses: List<Course>?, error: NetworkError?) -> Unit) {

        fun _fetch() {
            APIClient.instance.single<CoursesResponse>(APIEndpoint.Courses) { result, error ->
                courses = result?.courses
                onCompletion(courses, error)
            }
        }

        if (force || courses == null) {
            _fetch()
        } else {
            onCompletion(courses, null)
        }

    }

    fun start(studentId: String, selected: Course, onCompletion: (course: Course?, error: NetworkError?) -> Unit) {
        this.studentId = studentId
        courseCode = selected.code

        fetchByCourseCode { course, error ->
            course?.let {
                courseSelected = it
            }
            onCompletion(courseSelected, error)
        }
    }

    private fun fetchByCourseCode(onCompletion: (course: Course?, error: NetworkError?) -> Unit) {
        APIClient.instance.single<CourseByCodeResponse>(APIEndpoint.CourseByCode(studentId!!, courseCode!!)) { result, error ->
            onCompletion(result?.course, error)
        }
    }

    fun submit(onCompletion: (success: Boolean) -> Unit) {
        APIClient.instance.single<CourseByCodeResponse>(APIEndpoint.Submit(studentId!!, courseSelected!!)) { result, error ->
            result?.let {
                courseSelected = it.course
                onCompletion(true)
            } ?: onCompletion(false)
        }
    }
}