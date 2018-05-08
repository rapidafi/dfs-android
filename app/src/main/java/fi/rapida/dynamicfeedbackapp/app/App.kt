package fi.rapida.dynamicfeedbackapp.app

import android.app.Application
import fi.rapida.dynamicfeedbackapp.R
import io.upify.utils.general.Utils

/**
 * Created by egenesio on 19/04/2018.
 */
class App: Application() {

    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        Utils.init(this).apply {
            errorTitle = getString(R.string.general_error_title)
            errorMessage = getString(R.string.general_error_message)
            errorOkButton = getString(R.string.general_btn_ok)
        }
    }

}