package fi.rapida.dynamicfeedbackapp.ui.splash

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import fi.rapida.dynamicfeedbackapp.ui.login.WelcomeActivity
import org.jetbrains.anko.startActivity

/**
 * Created by egenesio on 19/04/2018.
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initUI()
    }

    private fun initUI() {
        startActivity<WelcomeActivity>()
    }
}