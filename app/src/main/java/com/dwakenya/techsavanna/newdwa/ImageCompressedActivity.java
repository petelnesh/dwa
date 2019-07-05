package com.dwakenya.techsavanna.newdwa;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cocosw.bottomsheet.BottomSheet;
import com.dwakenya.techsavanna.newdwa.app.AppConfig;
import com.dwakenya.techsavanna.newdwa.app.AppController;
import com.dwakenya.techsavanna.newdwa.helpers.CommonUtils;

import net.gotev.uploadservice.MultipartUploadRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static com.loopj.android.http.AsyncHttpClient.LOG_TAG;

public class ImageCompressedActivity extends AppCompatActivity {


    private static final String TAG = "Photo";
    private FloatingActionButton mbtnCapture;
    private Button btnSubmit;
    private ImageView mivImage;
    private Context mContext;
    private final int REQUEST_CODE_CLICK_IMAGE = 1002, REQUEST_CODE_GALLERY_IMAGE = 1003;
    private Uri fileUri; // file url to store image
    public static String imageFilePath;//Made it static as need to override the original image with compressed image.
    private BottomSheet mBottomSheetDialog;

    private String path="";

    private static final int WRITE_PERMISSION = 0x01;

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_compressed);

        requestWritePermission();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Registration Step 4/4");

        mContext = ImageCompressedActivity.this;
        mbtnCapture = (FloatingActionButton) findViewById(R.id.button);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (path.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "No Image is Selected", Toast.LENGTH_LONG).show();
                } else {
                    signupDoc();

                }
            }
        });
        mivImage = (ImageView) findViewById(R.id.imageView);
        imageFilePath = CommonUtils.getFilename();

        Log.d("Image Path===", imageFilePath);
        mbtnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionBottomSheetDialog();
            }
        });
        createBottomSheetDialog();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(requestCode == WRITE_PERMISSION){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(LOG_TAG, "Write Permission Failed");
                //finish();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestWritePermission(){
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_PERMISSION);
        }
    }

    /**
     * Request for camera app to open and capture image.
     * @param isFromGallery-if true then launch gallery app else camera app.
     */
    public void startIntent(boolean isFromGallery) {
//        if (!isFromGallery) {
//            File imageFile = new File(imageFilePath);
//
//            fileUri = FileProvider.getUriForFile(ImageCompressedActivity.this, BuildConfig.APPLICATION_ID,imageFile); // convert path to Uri
//
////            Uri imageFileUri = Uri.fromFile(imageFile); // convert path to Uri
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);   // set the image file name
//
////            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);   // set the image file name
//            startActivityForResult(intent, REQUEST_CODE_CLICK_IMAGE);
//        }
        //else
        if (isFromGallery) {
            File imageFile = new File(imageFilePath);
            Uri imageFileUri = Uri.fromFile(imageFile); // convert path to Uri
            Intent intent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);   // set the image file name
            startActivityForResult(
                    Intent.createChooser(intent, "Select File"),
                    REQUEST_CODE_GALLERY_IMAGE);
        }
    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    /*
     * Here we restore the fileUri again
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CLICK_IMAGE) {
            new ImageCompression().execute(imageFilePath);
        } else if (requestCode == REQUEST_CODE_GALLERY_IMAGE
                && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(projection[0]);
            final String picturePath = cursor.getString(columnIndex); // returns null
            cursor.close();
            //copy the selected file of gallery into app's sdcard folder and perform the compression operations on it.
            //And override the original image with the newly resized image.
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        CommonUtils.copyFile(picturePath, imageFilePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            new ImageCompression().execute(imageFilePath);

        }
    }


    /**
     * Show online options with the bottom sheet dialog with hangout,viber,skype calling options
     */
    private void showOptionBottomSheetDialog() {
        mBottomSheetDialog.show();
    }

    /**
     * Create a bottomsheet dialog.
     */
    public void createBottomSheetDialog() {
        BottomSheet.Builder builder = new BottomSheet.Builder(ImageCompressedActivity.this).title("Choose Option").sheet(R
                .menu
                .image_selection_option).listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case R.id.it_gallery:
                        startIntent(true);
                        break;
                    case R.id.it_cancel:

                        break;
                }
                CommonUtils.hideKeyboard(ImageCompressedActivity.this);
            }
        });
        mBottomSheetDialog = builder.build();
    }

    /**
     * Asynchronos task to reduce an image size without affecting its quality and set in imageview.
     */
    public class ImageCompression extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            if (strings.length == 0 || strings[0] == null)
                return null;

            return CommonUtils.compressImage(strings[0]);
        }

        protected void onPostExecute(String imagePath) {
            // imagePath is path of new compressed image.

            //Set this as path for file upload

            path = new File(imagePath).getAbsolutePath();


            mivImage.setImageBitmap(BitmapFactory.decodeFile(new File(imagePath).getAbsolutePath()));
        }
    }

    @Override
    protected void onStop() {
        if (mBottomSheetDialog != null) {
            if (mBottomSheetDialog.isShowing()) {
                mBottomSheetDialog.hide();
            }
        }
        super.onStop();
    }


    public void uploadMultipart() {
        //getting name for the image
        final String uploadId = UUID.randomUUID().toString();
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmss", Locale.US);
        final String name = "IMG_" + formatter.format(today)+ "_"+AppConfig.preferences.getString(AppConfig.IDNO, null);

        //getting the actual path of the image
        //  String path = getPath(filePath);

        if (path.equals("")) {
            Toast.makeText(this, "Image Path Failed", Toast.LENGTH_SHORT).show();
        } else {


            //Uploading code
            try {

                //Creating a multi part request
                new MultipartUploadRequest(this, uploadId, AppConfig.SERVER_URL + "imageUpload.php")
                        .addFileToUpload(path, "image") //Adding file
                        .addParameter("name", name)
                        .addParameter("server_url", AppConfig.SERVER_URL) //send serverUrl dynamically
                        .addParameter("session_link", "") //Adding text parameter to the request
                        .addParameter("user_email", AppConfig.preferences.getString(AppConfig.EMAIL, null))
                        .addParameter("image_type", "registration")
                        //  .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(0)
                        .startUpload(); //Starting the upload




            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }


    }


    public void signupDoc() {
        Log.d(TAG, "Signup");


        Thread thread = new Thread() {
            ProgressDialog progressDialog;
            Handler handler = new Handler();
            boolean error;

            @Override
            public void run() {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        progressDialog = new ProgressDialog(ImageCompressedActivity.this,
                                R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("Creating Account...");
                        progressDialog.show();
                    }
                });

                HashMap<String, String> params = new HashMap<String, String>();

                // Type - 1 representing employee
                params.put("user_type", "1");
                params.put("first_name", AppConfig.preferences.getString(AppConfig.FIRST_NAME, null));
                params.put("last_name", AppConfig.preferences.getString(AppConfig.LAST_NAME, null));
                params.put("id_no", AppConfig.preferences.getString(AppConfig.IDNO, null));
                params.put("phone_cell", AppConfig.preferences.getString(AppConfig.PHONE, null));
                params.put("email1", AppConfig.preferences.getString(AppConfig.EMAIL, null));
                params.put("region", AppConfig.preferences.getString(AppConfig.COUNTY_TOWN, null));
                params.put("coordinates", AppConfig.preferences.getString(AppConfig.COORDINATES, null));
                params.put("locname", AppConfig.preferences.getString(AppConfig.LOCNAME, null));


                params.put("age", AppConfig.preferences.getString(AppConfig.AGE, null));
                params.put("gender", AppConfig.preferences.getString(AppConfig.GENDER, null));
                params.put("organization", AppConfig.preferences.getString(AppConfig.ORGANIZATION, null));
                params.put("marital_status", AppConfig.preferences.getString(AppConfig.MARITAL_STATUS, null));
                params.put("tribe", AppConfig.preferences.getString(AppConfig.TRIBE, null));


                params.put("city", AppConfig.preferences.getString(AppConfig.CITY, null));
                params.put("state", AppConfig.preferences.getString(AppConfig.STATE, null));
                params.put("date_available", AppConfig.preferences.getString(AppConfig.AVAILABLE_FROM, null));
                params.put("can_relocate", AppConfig.preferences.getString(AppConfig.CAN_RELOCATE, null));
                params.put("current_employer", AppConfig.preferences.getString(AppConfig.CURRENT_EMPLOYER, null));
                params.put("desired_pay", AppConfig.preferences.getString(AppConfig.DESIRED_PAY, null));
                params.put("current_pay", AppConfig.preferences.getString(AppConfig.CURRENT_PAY, null));
                params.put("best_time_to_call", AppConfig.preferences.getString(AppConfig.TIME_TO_CALL, null));



                params.put("length", AppConfig.preferences.getString(AppConfig.LENGTH, null));
                params.put("reason", AppConfig.preferences.getString(AppConfig.REASON, null));
                params.put("referee", AppConfig.preferences.getString(AppConfig.REFEREE, null));
                // params.put("religion", AppConfig.preferences.getString(AppConfig.RELIGION, null));
                params.put("children", AppConfig.preferences.getString(AppConfig.CHILDREN, null));
                params.put("image_type", AppConfig.preferences.getString(AppConfig.PROFILE_IMAGE, null));
                params.put("password", AppConfig.preferences.getString(AppConfig.PASSWORD, null));



                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
                        AppConfig.SERVER_URL + "registration.php", new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(final JSONObject response) {
                                progressDialog.dismiss();
                                Log.d(TAG, " On Response: " + response.toString());
                                try {

                                    // Json parsing from response
                                    JSONObject object = new JSONObject(response.toString());

                                    // check error
                                    error = object.getBoolean("error");
                                    if (!error) {

                                        uploadMultipart();

                                        Toast.makeText(getBaseContext(), "Registration Successful", Toast.LENGTH_LONG).show();

                                        Intent i = new Intent(ImageCompressedActivity.this,LoginActivity.class);
                                        startActivity(i);
                                        finish();


                                    } else {
                                        Toast.makeText(getBaseContext(), "Registration failed : " + object.getString("error_msg"), Toast.LENGTH_LONG).show();
                                    }
                                    VolleyLog.v("Response:%n %s", response.toString(4));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        NetworkResponse errorRes = error.networkResponse;
                        String stringData = "";
                        if (errorRes != null && errorRes.data != null) {
                            try {
                                stringData = new String(errorRes.data, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.e("Error", stringData);

                        Log.e(TAG, "On ErrorResponse: " + error.getMessage());
                        VolleyLog.e("Error: ", error.getMessage());
                        Toast.makeText(ImageCompressedActivity.this, "No network connection", Toast.LENGTH_SHORT).show();
                    }
                }) {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Accept", "application/json");
                        headers.put("Content-Type", "application/json; charset=utf-8");

                        return headers;
                    }

                };

                // add the request object to the queue to be executed
                AppController.getInstance().addToRequestQueue(req);
            }

        };
        thread.start();
    }
}
