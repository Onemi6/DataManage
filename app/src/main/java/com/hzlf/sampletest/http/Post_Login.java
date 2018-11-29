package com.hzlf.sampletest.http;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Post_Login {
    @Headers({
            "accept: application/json",
            "Content-Type: application/json"})
    @POST("api/Login")
    Call<ResponseBody> getCall(@Body RequestBody body);
}
