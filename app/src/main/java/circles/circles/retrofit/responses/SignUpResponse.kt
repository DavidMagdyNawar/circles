package circles.circles.retrofit.responses

import circles.circles.retrofit.errors.SignUpErros
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class SignUpResponse (

    @SerializedName("message")
    @Expose
    var message: String,
    @SerializedName("errors")
    @Expose
    var errors: SignUpErros

)