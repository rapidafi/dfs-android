package fi.rapida.dynamicfeedbackapp.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fi.rapida.dynamicfeedbackapp.R
import fi.rapida.dynamicfeedbackapp.domain.CourseConcept
import fi.rapida.dynamicfeedbackapp.domain.FeelingValue
import fi.rapida.dynamicfeedbackapp.domain.MasteryValue
import fi.rapida.dynamicfeedbackapp.domain.SignificanceValue
import io.upify.utils.ui.extensions.inputText
import kotlinx.android.synthetic.main.fragment_concept.*

/**
 * Created by egenesio on 23/04/2018.
 */
class ConceptFragment: Fragment(), View.OnClickListener {

    private val activity: MainActivity? get() =  getActivity() as? MainActivity

    lateinit var concept: CourseConcept

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_concept, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        actions()
    }

    private fun initUI() {
        when(concept.disposition.feeling) {
            FeelingValue.NEGATIVE -> segmentedFeeling0.isChecked = true
            FeelingValue.NEUTRAL -> segmentedFeeling1.isChecked = true
            FeelingValue.POSITIVE -> segmentedFeeling2.isChecked = true
        }

        when(concept.disposition.significance) {
            SignificanceValue.NOT_IMPORTANT -> segmentedSignificance0.isChecked = true
            SignificanceValue.NEUTRAL -> segmentedSignificance1.isChecked = true
            SignificanceValue.IMPORTANT -> segmentedSignificance2.isChecked = true
        }

        when(concept.disposition.mastery) {
            MasteryValue.NEVER_HEARD -> segmentedMastery0.isChecked = true
            MasteryValue.FAMILIAR -> segmentedMastery1.isChecked = true
            MasteryValue.GOT_IT -> segmentedMastery2.isChecked = true
        }

        txtComment.inputText = concept.disposition.comment
    }

    private fun actions() {
        btnBack.setOnClickListener {
            activity?.goPreviousPage()
        }

        btnNext.setOnClickListener {
            activity?.goNextPage()
        }

        segmentedFeeling0.setOnClickListener(this)
        segmentedFeeling1.setOnClickListener(this)
        segmentedFeeling2.setOnClickListener(this)

        segmentedSignificance0.setOnClickListener(this)
        segmentedSignificance1.setOnClickListener(this)
        segmentedSignificance2.setOnClickListener(this)

        segmentedMastery0.setOnClickListener(this)
        segmentedMastery1.setOnClickListener(this)
        segmentedMastery2.setOnClickListener(this)

        txtComment.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                concept.disposition.comment = txtComment.inputText
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onClick(v: View?) = when(v?.id) {
        R.id.segmentedFeeling0 -> concept.disposition.setFeeling(-1)
        R.id.segmentedFeeling1 -> concept.disposition.setFeeling(0)
        R.id.segmentedFeeling2 -> concept.disposition.setFeeling(1)

        R.id.segmentedSignificance0 -> concept.disposition.setSignificance(-1)
        R.id.segmentedSignificance1 -> concept.disposition.setSignificance(0)
        R.id.segmentedSignificance2 -> concept.disposition.setSignificance(1)

        R.id.segmentedMastery0 -> concept.disposition.setMastery(0)
        R.id.segmentedMastery1 -> concept.disposition.setMastery(1)
        R.id.segmentedMastery2 -> concept.disposition.setMastery(2)
        else -> {}
    }

}