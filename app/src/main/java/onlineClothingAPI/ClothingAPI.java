package onlineClothingAPI;

import java.util.List;

import model.ImageResponse;
import model.Item;
import model.LoginSignUpResponse;
import model.User;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ClothingAPI {

//  using object
    @FormUrlEncoded
    @POST("heroes")
    Call<Void> addItems(@Header("Cookie") String cookie, @Field("name") String name, @Field("image") String image, @Field("desc") String desc);

 //    for uploading image
    @Multipart
    @POST("upload")
    Call<ImageResponse> uploadImage(@Header("Cookie") String cookie, @Part MultipartBody.Part img);

// get all items
    @GET("heroes")
    Call<List<Item>> getAllItems(@Header("Cookie") String cookie);

//  for login
    @FormUrlEncoded
    @POST("users/login")
    Call<LoginSignUpResponse> checkUser(@Field("username") String email, @Field("password") String password);

//    for signup

    @FormUrlEncoded
    @POST("users/signup")
    Call<LoginSignUpResponse> registerUser(@Field("username") String username,@Field("password") String password);

//    for logout
    @GET("users/logout")
    Call<Void> logout(@Header("Cookie") String cookie);



}
