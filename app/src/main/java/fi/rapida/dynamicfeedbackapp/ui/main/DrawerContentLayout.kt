package fi.rapida.dynamicfeedbackapp.ui.main

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import fi.rapida.dynamicfeedbackapp.R
import fi.rapida.dynamicfeedbackapp.domain.Course
import fi.rapida.dynamicfeedbackapp.domain.CourseConcept
import fi.rapida.dynamicfeedbackapp.domain.CoursePage
import fi.rapida.dynamicfeedbackapp.session.SessionManager
import io.upify.utils.ui.adapter.HeaderAdapter
import io.upify.utils.ui.adapter.Section
import io.upify.utils.ui.adapter.SectionedHeaderAdapter
import io.upify.utils.ui.extensions.ImageScaleType
import io.upify.utils.ui.extensions.set
import kotlinx.android.synthetic.main.item_drawer_header.view.*
import kotlinx.android.synthetic.main.item_drawer_item.view.*
import org.jetbrains.anko.textColor
import kotlin.properties.Delegates

/**
 * Created by egenesio on 23/04/2018.
 */
class DrawerContentLayout : ConstraintLayout {

    companion object {

    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private lateinit var mainActivity: MainActivity
    private lateinit var rcvDrawer: RecyclerView

    private val list: List<CoursePage> get() = SessionManager.instance.coursePages
    private var currentPage: Int = 0

    private val adapter: HeaderAdapter<CoursePage> by lazy {
        HeaderAdapter<CoursePage>(Pair(R.layout.item_drawer_header, R.layout.item_drawer_item)) }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        initUI()
    }

    private fun initUI() {
        mainActivity = context as MainActivity
        setOnClickListener {}

        adapter.listenerHeader = this::setupHeader
        adapter.listenerItem = this::setupItem

        rcvDrawer = mainActivity.findViewById(R.id.rcvDrawer)
        rcvDrawer.adapter = adapter

        val layoutManager = LinearLayoutManager(context)
        rcvDrawer.adapter = adapter
        rcvDrawer.layoutManager = layoutManager
        rcvDrawer.setHasFixedSize(true)
        rcvDrawer.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))

        adapter.list = list
    }

    private fun setupHeader(viewHolder: HeaderAdapter.ViewHolder) {
        with(viewHolder.itemView) {
            lblStudentId.text = SessionManager.instance.studentId ?: ""
            lblCourseName.text = SessionManager.instance.courseSelected?.name ?: ""

            imgExit.setOnClickListener {
                mainActivity.exit()
            }
        }
    }

    private fun setupItem(viewHolder: HeaderAdapter.ViewHolder, page: CoursePage) {
        val index = list.indexOf(page)
        val textColor = context.resources.getColor(if (index == currentPage) R.color.colorGreen else android.R.color.black)
        with(viewHolder.itemView) {

            lblCourseConceptName.text = page.title
            lblCourseConceptName.textColor = textColor

            imgConceptStatus.setImageResource(if (page.isDone) R.drawable.ic_check else R.drawable.ic_check_outline)

            setOnClickListener {
                mainActivity.goToPage(index)
            }
        }
    }

    fun update(currentPage: Int) {
        this.currentPage = currentPage
        adapter.list = list
    }

}