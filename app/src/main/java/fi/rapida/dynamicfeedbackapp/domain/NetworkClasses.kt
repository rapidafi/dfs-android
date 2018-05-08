package fi.rapida.dynamicfeedbackapp.domain

import com.google.gson.annotations.SerializedName
import io.upify.utils.domain.NetworkErrorBase

/**
 * Created by egenesio on 19/04/2018.
 */
data class NetworkError(
        @SerializedName("status") var status: Int? = null,
        @SerializedName("devMessage") val devMessage: String? = null,
        @SerializedName("userMessage") val userMessage: String? = null): NetworkErrorBase {


    companion object {
        val default: NetworkError by lazy { NetworkError() }
        fun defaultWithCode(code: Int?): NetworkError? = code?.let { NetworkError(status = it) }
    }

    override val isValid: Boolean get() = true
    override val message: String get() = userMessage ?: "Error: $code" //TODO: change

    override val code: Int get() = status ?: 500
    override val mustLogout: Boolean get() = code in 401 .. 403
}