package com.dwakenya.techsavanna.newdwa.helpers;


import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by Padie on 9/27/2017.
 */

public class OkhttpHelper {

    String user_id, fcm_token;

    public OkhttpHelper(String user_id, String fcm_token ) {
        this.user_id=user_id;
        this.fcm_token=fcm_token;
    }

    public void updateUserTokenOnServer() {


        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("id", user_id)
                .add("fcm_token", fcm_token)
                .build();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("http://techsavanna.technology/dwa/fcm_registration.php")
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
