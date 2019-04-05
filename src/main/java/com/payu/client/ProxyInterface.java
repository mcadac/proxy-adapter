package com.payu.client;

import retrofit2.*;
import retrofit2.http.*;

import com.payu.model.*;

public interface ProxyInterface {


    @GET("/proxy")
    Call<String> getPriority();

    @POST("/proxy/leader")
    Call<String> postLeader(@Body Leader leader);


}
