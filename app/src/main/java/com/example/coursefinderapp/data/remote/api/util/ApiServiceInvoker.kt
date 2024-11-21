import android.util.Log
import com.example.coursefinderapp.domain.util.Event
import retrofit2.Response

suspend fun <T : Any> doCall(call: suspend () -> Response<T>): Event<T> {
    val response = call()
    return if (response.isSuccessful) {
        val body = response.body()
        if (body != null) {
            Event.Success(body)
        } else {
            Event.Failure("Unknown error")
        }
    } else {
        val errorBody = response.errorBody()?.string()
        if (!errorBody.isNullOrBlank()) {
            val apiError = errorBody.toApiError()
            Event.Failure(apiError.message)
        } else {
            Event.Failure("Unknown error")
        }
    }
}
