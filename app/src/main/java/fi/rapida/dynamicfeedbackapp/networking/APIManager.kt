package fi.rapida.dynamicfeedbackapp.networking

import fi.rapida.dynamicfeedbackapp.domain.Course
import fi.rapida.dynamicfeedbackapp.domain.NetworkError
import io.upify.utils.networking.APIClientBase
import io.upify.utils.networking.APIEndpointBase
import io.upify.utils.networking.HTTPMethod
import io.upify.utils.networking.single
import java.io.File

/**
 * Created by egenesio on 19/04/2018.
 */
sealed class APIEndpoint: APIEndpointBase() {

    override val httpMethod: HTTPMethod get() = HTTPMethod.GET
    override val isPrivate: Boolean get() = false
    override val body: Any? get() = null
    override val responseKey: String? get() = null
    override val fileToUpload: File? get() = null
    override val fileFieldName: String? get() = null

    object Courses: APIEndpoint() {
        override val method: String get() = ""
    }

    class CourseByCode(private val studentId: String, private val courseCode: String): APIEndpoint() {
        override val method: String get() = "$courseCode/$studentId"
    }

    class Submit(private val studentId: String, private val course: Course): APIEndpoint() {
        override val method: String get() = "${course.code}/$studentId"
        override val httpMethod: HTTPMethod get() = HTTPMethod.POST
        override val body: Any? get() = course
    }
}

class APIClient: APIClientBase<NetworkError>() {

    companion object {
        val instance = APIClient()
    }

    override val baseURL: String get() = "https://cma.rapida.fi/dynamicfeedback/"
    override val headerAccessToken: String? get() = null
    override val accessToken: String? get() = null
    override val extraHeaders: List<Pair<String, String>>? get() = null

    override val errorKey: String? get() = "error"

    override val logEnabled: Boolean get() = true

    override fun error(response: String?, code: Int?): NetworkError =
            jsonParser.single<NetworkError>(response, errorKey)?.apply { this.status = code ?: this.status }
                    ?: NetworkError.defaultWithCode(code)
                    ?: NetworkError.default
}