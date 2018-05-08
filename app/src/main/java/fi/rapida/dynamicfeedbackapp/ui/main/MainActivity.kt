package fi.rapida.dynamicfeedbackapp.ui.main

import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import fi.rapida.dynamicfeedbackapp.R
import fi.rapida.dynamicfeedbackapp.domain.CoursePageType
import fi.rapida.dynamicfeedbackapp.session.SessionManager
import fi.rapida.dynamicfeedbackapp.ui.login.WelcomeActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.action_bar.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    companion object {
        private val TABS_FRAGMENT_TAG = "TABS_FRAGMENT_TAG"
    }



    private lateinit var drawerToggle: DrawerToggle
    //private val courseSelectedListener by lazy { Pair(toString(), ::onCourseSelected) }

    private val adapter: PageAdapter by lazy { PageAdapter(supportFragmentManager) }

    private val submitPage: SubmitPageFragment by lazy { SubmitPageFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI()
        initData()
        actions()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (drawerToggle.onOptionsItemSelected(item)) true else super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (pager.currentItem >= 1) {
            goPreviousPage()
        } else {
            super.onBackPressed()
        }
    }

    private fun initUI() {
        drawerToggle = DrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close)

        drawerLayout.addDrawerListener(drawerToggle)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.action_bar)

        pager.adapter = adapter

        updatePage()
    }

    private fun initData() {

    }

    private fun actions() {

    }

    private fun closeDrawer() {
        drawerLayout.closeDrawer(Gravity.START, true)
    }

    fun goNextPage() {
        SessionManager.instance.coursePages[pager.currentItem].isDone = true
        pager.setCurrentItem(pager.currentItem + 1, true)
        updatePage()
    }

    fun goPreviousPage() {
        pager.setCurrentItem(pager.currentItem - 1, true)
        updatePage()
    }

    fun goToPage(index: Int) {
        pager.setCurrentItem(index, true)
        closeDrawer()
        updatePage()
    }

    fun exit() {
        startActivity<WelcomeActivity>()
        finish()
    }

    private fun updatePage() {
        val page = SessionManager.instance.coursePages[pager.currentItem]
        lblActionBarTitle.text = "${page.title}"
        lblActionBarIndicator.text = "${pager.currentItem + 1}/${adapter.count}"

        (contentDrawer as DrawerContentLayout).update(pager.currentItem)

        if (pager.currentItem == adapter.count - 1) {
            submitPage.setup()
        }
    }

    private class DrawerToggle(val activity: MainActivity, drawerLayout: DrawerLayout, openDrawer: Int, closeDrawer: Int):
            ActionBarDrawerToggle(activity, drawerLayout, openDrawer, closeDrawer) {

        override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            super.onDrawerSlide(drawerView, slideOffset)

            //activity.updateTitle(slideOffset)
        }

        override fun onDrawerClosed(drawerView: View) {
            super.onDrawerClosed(drawerView)

            //activity.switchDrawer(DrawerType.DEFAULT, false)
        }
    }

    private inner class PageAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            val page = SessionManager.instance.coursePages[position]

            return when(page.type) {
                CoursePageType.INTRO -> IntroPageFragment()
                CoursePageType.SUBMIT -> submitPage
                else -> {
                    ConceptFragment().apply {
                        concept = page.concept!!
                    }
                }
            }
        }

        override fun getCount(): Int = SessionManager.instance.coursePages.size
    }
}
