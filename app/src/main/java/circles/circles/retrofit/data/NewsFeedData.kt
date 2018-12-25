package circles.circles.retrofit.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class NewsFeedData (

    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("user_id")
    @Expose
    var userId: Int,
    @SerializedName("full_name")
    @Expose
    var fullName: String,
    @SerializedName("profile_image")
    @Expose
    var profileImage: Any,
    @SerializedName("title")
    @Expose
    var title: String,
    @SerializedName("text")
    @Expose
    var text: String,
    @SerializedName("image")
    @Expose
    var image: Any,
    @SerializedName("voice_note")
    @Expose
    var voiceNote: String,
    @SerializedName("created_at")
    @Expose
    var createdAt: Int,
    @SerializedName("is_like")
    @Expose
    var isLike: Boolean,
    @SerializedName("likes")
    @Expose
    var likes: Int,
    @SerializedName("is_shared")
    @Expose
    var isShared: Boolean,
    @SerializedName("shared_at")
    @Expose
    var sharedAt: Any,
    @SerializedName("shared_from")
    @Expose
    var sharedFrom: SharedFrom

    )