package circles.circles.retrofit.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse (

        @SerializedName("message")
        @Expose
        var message: String,
        
        @SerializedName("access_token")
        @Expose
        var accessToken: String,

        @SerializedName("token_type")
        @Expose
        var tokenType: String,

        @SerializedName("expires_at")
        @Expose
        var expiresAt: String

)