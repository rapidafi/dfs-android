package fi.rapida.dynamicfeedbackapp.ui.login

import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import fi.rapida.dynamicfeedbackapp.R
import fi.rapida.dynamicfeedbackapp.domain.Course
import fi.rapida.dynamicfeedbackapp.session.SessionManager
import fi.rapida.dynamicfeedbackapp.ui.main.MainActivity
import io.upify.utils.extensions.let
import io.upify.utils.ui.extensions.*
import kotlinx.android.synthetic.main.activity_welcome.*
import org.jetbrains.anko.startActivity
import kotlin.properties.Delegates


/**
 * Created by egenesio on 19/04/2018.
 */
class WelcomeActivity : AppCompatActivity() {

    private var isLoading: Boolean by Delegates.observable(false) { _, _, newValue ->
        if (newValue) {
            viewLoading.state = Visibility.VISIBLE
            imgLogo.state = Visibility.INVISIBLE
        } else {
            viewLoading.state = Visibility.GONE
            imgLogo.state = Visibility.VISIBLE
        }
    }

    private var courseSelected: Course? by Delegates.observable<Course?>(null) { _, _, newValue ->
        txtCourse.inputText = newValue?.name ?: ""
        enableStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        initUI()
        actions()
        fetchData()
    }

    private fun initUI() {
        txtCourse.disableInput()
        ViewCompat.setElevation(rootLayout, 20f)
        enableStart()
    }

    private fun actions() {
        textInputLayoutCourse.setOnClickListener {
            showCourses()
        }
        txtCourse.setOnClickListener {
            showCourses()
        }

        txtStudentNumber.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                enableStart()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        btnStart.setOnClickListener {
            courseSelected?.let {

                isLoading = true

                SessionManager.instance.start(txtStudentNumber.inputText, selected = it) { course, error ->
                    //isLoading = false
                    course?.let {
                        startActivity<MainActivity>()
                    } ?: alertError()
                }
            }
        }
    }

    private fun fetchData() {
        isLoading = true

        SessionManager.instance.fetchCourses { courses, error ->
            viewLoading ?: return@fetchCourses

            txtStudentNumber.inputText = SessionManager.instance.studentId ?: ""
            courseSelected = courses?.firstOrNull { it.code == SessionManager.instance.courseCode }

            isLoading = false
        }

    }

    private fun showCourses() {
        supportFragmentManager.showFragment(CoursesFragment(), R.id.rootLayout, FragmentAnim.TYPE_MODAL, CoursesFragment.TAG)
    }

    private fun enableStart() {
        btnStart.isEnabled = txtStudentNumber.inputText.length == SessionManager.STUDENT_ID_MAX && courseSelected != null
    }

    fun select(course: Course) {
        courseSelected = course
    }
}