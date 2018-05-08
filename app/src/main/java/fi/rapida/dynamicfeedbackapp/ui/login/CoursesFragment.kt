package fi.rapida.dynamicfeedbackapp.ui.login

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fi.rapida.dynamicfeedbackapp.R
import fi.rapida.dynamicfeedbackapp.domain.Course
import fi.rapida.dynamicfeedbackapp.session.SessionManager
import io.upify.utils.ui.adapter.SimpleAdapter
import io.upify.utils.ui.extensions.alertIfError
import io.upify.utils.ui.extensions.remove
import kotlinx.android.synthetic.main.fragment_courses.*
import kotlinx.android.synthetic.main.item_course.view.*
import kotlin.properties.Delegates

/**
 * Created by egenesio on 20/04/2018.
 */
class CoursesFragment: Fragment() {

    companion object {
        val TAG = "CoursesFragment"
    }

    private var courses: List<Course>? by Delegates.observable<List<Course>?>(null) { _, _, newValue ->
        adapter.list = courses ?: listOf()
    }

    private val adapter: SimpleAdapter<Course> by lazy { SimpleAdapter<Course>(R.layout.item_course) }

    private val activity: WelcomeActivity? by lazy { getActivity() as? WelcomeActivity }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_courses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        fetchData()
        actions()
    }

    private fun initUI() {
        adapter.listener = ::setupItem

        val layoutManager = LinearLayoutManager(context)

        rcvCourses.adapter = adapter
        rcvCourses.layoutManager = layoutManager
        rcvCourses.setHasFixedSize(true)
        rcvCourses.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))
        refreshView.setOnRefreshListener { fetchData() }
    }

    private fun fetchData() {
        refreshView.isRefreshing = true

        SessionManager.instance.fetchCourses { list, error ->
            courses = list
            alertIfError(error)

            refreshView.isRefreshing = false
        }
    }

    private fun actions() {
        view?.setOnClickListener {  }

        imgActionLeft.setOnClickListener {
            activity?.supportFragmentManager?.remove(this, true)
        }
    }

    private fun setupItem(viewHolder: SimpleAdapter.ViewHolder, item: Course) {
        with(viewHolder.itemView) {
            lblCourseName.text = item.name

            setOnClickListener {
                activity?.select(item)
                activity?.supportFragmentManager?.remove(this@CoursesFragment, true)
            }
        }
    }



}