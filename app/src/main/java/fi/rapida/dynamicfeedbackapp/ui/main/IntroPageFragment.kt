package fi.rapida.dynamicfeedbackapp.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fi.rapida.dynamicfeedbackapp.R
import fi.rapida.dynamicfeedbackapp.domain.CourseConcept
import fi.rapida.dynamicfeedbackapp.session.SessionManager
import kotlinx.android.synthetic.main.fragment_page_intro.*

/**
 * Created by egenesio on 23/04/2018.
 */
class IntroPageFragment : Fragment() {

    private val activity: MainActivity? get() =  getActivity() as? MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_page_intro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        actions()
    }

    private fun initUI() {
        lblTitle.text = SessionManager.instance.courseSelected?.name ?: ""
    }

    private fun actions() {
        btnNext.setOnClickListener {
            activity?.goNextPage()
        }
    }

}