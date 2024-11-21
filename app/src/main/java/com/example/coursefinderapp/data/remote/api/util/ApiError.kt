import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class ApiError(
    @SerializedName("code")
    val errorCode: Int,
    @SerializedName("message")
    val message: String
)

fun String.toApiError(): ApiError = Gson().fromJson(this, ApiError::class.java)

enum class ApiErrorCode(val errorMessage: String) {
    EMAIL_EXISTS( "EMAIL_EXISTS"),
    UNKNOWN_ERROR("UNKNOWN_ERROR");

    companion object {
        fun fromMessage(message: String): ApiErrorCode {
            return values().firstOrNull { message.contains(it.errorMessage, ignoreCase = true) }
                ?: UNKNOWN_ERROR
        }
    }
}
