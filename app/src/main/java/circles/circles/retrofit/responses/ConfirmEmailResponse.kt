package circles.circles.retrofit.responses

import circles.circles.retrofit.errors.ConfirmUserErrorrs
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ConfirmEmailResponse (

    @SerializedName("message")
    @Expose
    var message: String,
    @SerializedName("errors")
    @Expose
    var errors: ConfirmUserErrorrs

)