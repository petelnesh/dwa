package com.dwakenya.techsavanna.newdwa.helpers;

import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dwakenya.techsavanna.newdwa.app.AppConfig;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by padie on 4/6/2017.
 */

public class CameraHelper {

    /**
     * Creating file uri to store image/video
     */
    public static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }
    /**
     * returning image / video
     */
    @Nullable
    private static File getOutputMediaFile(int type) {

        // Use Internal Storage if external does not exist
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), AppConfig.IMAGE_DIRECTORY_NAME);


        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Tag", "Oops! Failed create External Directory"
                        + AppConfig.IMAGE_DIRECTORY_NAME + " Using Internal SD");

                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == AppConfig.MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == AppConfig.MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

}
