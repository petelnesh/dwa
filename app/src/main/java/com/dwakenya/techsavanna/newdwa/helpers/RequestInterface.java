package com.dwakenya.techsavanna.newdwa.helpers;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Padie on 8/1/2017.
 */

public interface RequestInterface {

    @GET("android/jsonandroid")
    Call<JSONResponse> getJSON();
}
