package com.hzlf.sampletest.http;

import com.hzlf.sampletest.entityclass.Status;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Post_Login {
    @POST("/api/Login")
    @FormUrlEncoded
    Call<Status> getCall(@Field("loginName") String loginName,
                         @Field("passWord") String passWord);
}
