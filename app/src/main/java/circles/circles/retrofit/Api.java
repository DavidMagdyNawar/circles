package circles.circles.retrofit;

import java.util.Map;

import circles.circles.retrofit.responses.AddPostResponse;
import circles.circles.retrofit.responses.ConfirmEmailResponse;
import circles.circles.retrofit.responses.GetUserLikedPostsResponse;
import circles.circles.retrofit.responses.GetUsersPostsResponse;
import circles.circles.retrofit.responses.LikedAndDislikeResponse;
import circles.circles.retrofit.responses.LoginResponse;
import circles.circles.retrofit.responses.NewsFeedResponse;
import circles.circles.retrofit.responses.SignUpResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface Api {

    @FormUrlEncoded
    @Headers({
            "Accept: application/json",
            "Enctype: multipart/form-data"
    })
    @POST("signup")
    Call<SignUpResponse> createUser(
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("city") String city,
            @Field("country") String country,
            @Field("birthdate") String birthdate,
            @Field("username") String username,
            @Field("gender") Boolean gender,
            @Field("password") String password,
            @Field("password_confirmation") String password_confirmation

    );

    @FormUrlEncoded
    @POST("login")
    @Headers("Accept: application/json")
    Call<LoginResponse> login(
            @Field("email") String email,
            @Field("password") String password
    );

//    @Multipart
//    @POST("signup")
//    Call<ResponseBody> postImage(@Part MultipartBody.Part image, @Part("name") RequestBody name);

    @FormUrlEncoded
    @POST("confirmUser")
    @Headers("Accept: application/json")
    Call<ConfirmEmailResponse> confirmEmail(
            @Field("email") String email,
            @Field("code") String code
    );


    @POST("getNewsFeed")
    @Headers("Accept: application/json")
    Call<NewsFeedResponse> getNewsfeed(@Header("Authorization") String accessToken_And_Type);


    @POST("getPosts")
    @Headers("Accept: application/json")
    Call<NewsFeedResponse> getUserPosts(@Header("Authorization") String accessToken_And_Type);


    @POST("getLikedPosts")
    @Headers("Accept: application/json")
    Call<NewsFeedResponse> getUserLikedPosts(@Header("Authorization") String accessToken_And_Type);

    @FormUrlEncoded
    @POST("likeOrDislikePost")
    @Headers("Accept: application/json")
    Call<LikedAndDislikeResponse> likeAndDislikePost(@Header("Authorization") String accessToken_And_Type
            , @Field("id") String id);


    @FormUrlEncoded
    @POST("sharePost")
    @Headers("Accept: application/json")
    Call<LikedAndDislikeResponse> sharePost(@Header("Authorization") String accessToken_And_Type
            , @Field("id") String id);


    @POST("addPost")
    @Multipart
    @Headers({"Accept: application/json","Enctype: multipart/form-data"})
    Call<ResponseBody> addPost(@Header("Authorization") String accessToken_And_Type,
                               @Part MultipartBody.Part file);
}
