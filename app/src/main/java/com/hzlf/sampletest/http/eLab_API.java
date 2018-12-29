package com.hzlf.sampletest.http;

import com.hzlf.sampletest.model.AAQI;
import com.hzlf.sampletest.model.Codes;
import com.hzlf.sampletest.model.Source;
import com.hzlf.sampletest.model.Status;
import com.hzlf.sampletest.model.UpdateInfo;
import com.hzlf.sampletest.model.Upload;
import com.hzlf.sampletest.model.UploadImg;
import com.hzlf.sampletest.model.User;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface eLab_API {

    @GET("Emp")
    Call<List<User>> Emp(@Header("Authorization") String token);

    @GET("Code")
    Call<Codes> Code(@Header("Authorization") String token, @Query("no") String no, @Query("num")
            String num);

    @GET("Sys/GetAllSource")
    Call<List<Source>> GetAllSource(@Header("Authorization") String token);

    @GET("AAQI")
    Call<AAQI> AAQI(@Header("Authorization") String token, @Query("id") String id);

    @Headers({
            "accept: application/json",
            "Content-Type: application/json"})
    @POST("Login")
    Call<Status> Login(@Body RequestBody body);

    @POST("Apply")
    Call<Upload> Apply(@Header("Authorization") String token, @Body RequestBody body);

    @Multipart
    @POST("Apply/ImgUpload")
    Call<UploadImg> ApplyImgUpload(@Header("Authorization") String token, @QueryMap Map<String,
            String> params, @Part MultipartBody.Part file);

    @GET("update.xml")
    Call<UpdateInfo> UpdateXML();
}
