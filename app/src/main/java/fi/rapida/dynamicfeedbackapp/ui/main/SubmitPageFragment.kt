package fi.rapida.dynamicfeedbackapp.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fi.rapida.dynamicfeedbackapp.R
import fi.rapida.dynamicfeedbackapp.domain.*
import fi.rapida.dynamicfeedbackapp.networking.APIClient
import fi.rapida.dynamicfeedbackapp.networking.APIEndpoint
import fi.rapida.dynamicfeedbackapp.session.SessionManager
import io.upify.utils.ui.adapter.HeaderAdapter
import io.upify.utils.ui.extensions.Visibility
import io.upify.utils.ui.extensions.alertError
import io.upify.utils.ui.extensions.state
import kotlinx.android.synthetic.main.fragment_page_submit.*
import kotlinx.android.synthetic.main.item_submit_list.view.*
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.textColor
import kotlin.properties.Delegates

/**
 * Created by egenesio on 23/04/2018.
 */
class SubmitPageFragment : Fragment() {

    private val activity: MainActivity? get() =  getActivity() as? MainActivity

    private var list: List<CoursePage>? by Delegates.observable<List<CoursePage>?>(null) { _, _, newValue ->
        newValue?.let {
            adapter.list = it.subList(1, it.size - 1)
        }
    }

    private val adapter: HeaderAdapter<CoursePage> by lazy {
        HeaderAdapter<CoursePage>(Pair(R.layout.item_submit_header, R.layout.item_submit_list)) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_page_submit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        actions()
    }

    private fun initUI() {
        adapter.listenerHeader = this::setupHeader
        adapter.listenerItem = this::setupItem

        val layoutManager = LinearLayoutManager(context)
        rcvSubmit.adapter = adapter
        rcvSubmit.layoutManager = layoutManager
        rcvSubmit.setHasFixedSize(true)
        rcvSubmit.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))

        list = SessionManager.instance.coursePages
    }

    private fun actions() {
        btnBack.setOnClickListener {
            activity?.goPreviousPage()
        }

        btnSubmit.setOnClickListener {

            viewLoading.state = Visibility.VISIBLE

            SessionManager.instance.submit { success ->
                viewLoading.state = Visibility.GONE

                if (success) {
                    alert {
                        titleResource = R.string.page_submit_success_title
                        messageResource = R.string.page_submit_success_message
                        positiveButton(R.string.general_btn_ok) {
                            activity?.exit()
                        }
                    }.show()
                } else {
                    alertError()
                }
            }

        }
    }

    fun setup() {
        list = SessionManager.instance.coursePages
    }

    private fun setupHeader(viewHolder: HeaderAdapter.ViewHolder) {
        with(viewHolder.itemView) {

        }
    }

    private fun setupItem(viewHolder: HeaderAdapter.ViewHolder, page: CoursePage) {
        page.concept ?: return
        val index = list?.indexOf(page)?: return

        with(viewHolder.itemView) {
            lblCourseConceptName.text = page.title

            imgFeeling.imageResource = when(page.concept.disposition.feeling) {
                FeelingValue.NEGATIVE -> R.drawable.ic_feeling_neg
                FeelingValue.NEUTRAL -> R.drawable.ic_feeling_0
                FeelingValue.POSITIVE -> R.drawable.ic_feeling_1
            }

            imgSignificance.imageResource = when(page.concept.disposition.significance) {
                SignificanceValue.NOT_IMPORTANT -> 0
                SignificanceValue.NEUTRAL -> 0
                SignificanceValue.IMPORTANT -> R.drawable.ic_significance_1
            }

            imgMastery.imageResource = when(page.concept.disposition.mastery) {
                MasteryValue.NEVER_HEARD -> R.drawable.ic_mastery_0
                MasteryValue.FAMILIAR -> R.drawable.ic_mastery_1
                MasteryValue.GOT_IT -> R.drawable.ic_mastery_2
            }

            setOnClickListener {
                activity?.goToPage(index)
            }
        }
    }

}