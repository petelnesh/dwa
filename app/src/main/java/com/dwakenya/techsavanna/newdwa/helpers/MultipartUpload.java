package com.dwakenya.techsavanna.newdwa.helpers;

import android.content.Context;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.UUID;

/**
 * Created by padie on 3/17/2017.
 */

public class MultipartUpload {

    Context context;
    String server_url, file_path, sessionId, user_id;
    String section="general";

    public MultipartUpload(){

    }

    public MultipartUpload(String section){
        this.section=section;

    }


    public void uploadFile(final Context context, final String server_url, final String file_path, final String sessionId, final String user_id) {
        this.context=context;
        this.server_url=server_url;
        this.file_path= file_path;
        this.sessionId=sessionId;
        this.user_id = user_id;
        //Uploading code
        try {
            Thread t = new Thread() {
                @Override
                public void run() {
                    super.run();

                    String uploadId = UUID.randomUUID().toString();
                    //Creating a multi part request
                    try {
                        new MultipartUploadRequest(context, uploadId, server_url)
                                .addFileToUpload(file_path, "image") //Adding file
                                .addParameter("name", uploadId)
                                .addParameter("session_link", sessionId) //Adding text parameter to the request
                                 .addParameter("user_id", user_id)
                                  .addParameter("section", section)
                                .setNotificationConfig(new UploadNotificationConfig())
                                .setMaxRetries(2)
                                .startUpload(); //Starting the upload
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            };
            t.start();



        } catch (Exception exc) {
            System.out.println("Exception " + exc.toString());
        }
    }
}
