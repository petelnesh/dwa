package com.dwakenya.techsavanna.newdwa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;


import com.dwakenya.techsavanna.newdwa.app.AppConfig;
import com.dwakenya.techsavanna.newdwa.helpers.SessionManager;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class FeedBackActivity extends AppCompatActivity implements View.OnClickListener{

    RatingBar ratingBar;

    private String TAG = FeedBackActivity.class.getSimpleName();

    SessionManager session;

    HashMap<String, String> user;

    float rating;
    private Button btnSubmit;
    EditText etRSubject, etRComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Session Manager
        session = new SessionManager(getApplicationContext());

        user = session.getUserDetails();

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        etRSubject = findViewById(R.id.et_rating_subjct);
        etRComments = findViewById(R.id.et_rating_comments);

        btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);
        //ratingBar.setRating(5);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                rating = ratingBar.getRating();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

//        if (user.get(SessionManager.KEY_USER_TYPE).equals("1")) {
//            //Employee
//            Intent intent = new Intent(this, EmployeeHomeActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            finish();
//
//        } else if (user.get(SessionManager.KEY_USER_TYPE).equals("0")) {
//
//            Intent intent = new Intent(this, SearchActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            finish();
//
//        } else {
//
//            Intent intent = new Intent(this, LoginActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            finish();
//
//        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {


            case android.R.id.home:

                if (user.get(SessionManager.KEY_USER_TYPE).equals("1")) {
                    //Employee
                    Intent intent = new Intent(this, EmployeeHomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                } else if (user.get(SessionManager.KEY_USER_TYPE).equals("0")) {

                    Intent intent = new Intent(this, SearchActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                } else {

                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                }

                break;

            case R.id.action_logout:

                session.logoutUser();

                break;


            case R.id.action_feedback:

                Toast.makeText(getApplicationContext(), "Already here", Toast.LENGTH_SHORT).show();

                break;

            case R.id.action_refresh:


                break;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

        return true;

    }

    @Override
    public void onClick(View v) {

        if (v.equals(btnSubmit)) {
            if(rating > 0){
                //Valid

                Calendar calendar = Calendar.getInstance();
                Date today = calendar.getTime();

                SimpleDateFormat date_sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

                submitFeedback(FeedBackActivity.this,
                        user.get(SessionManager.KEY_USER_ID), date_sdf.format(today),
                        etRSubject.getText().toString(), user.get(SessionManager.KEY_EMAIL),
                        user.get(SessionManager.KEY_NAME), etRComments.getText().toString(), String.valueOf(rating));

            }else{

                Toast.makeText(getApplicationContext(), "Please rate us to proceed", Toast.LENGTH_LONG).show();

            }
        }

    }

    private void submitFeedback
            (Context context, String user_id, String date_created, String subject,
             String reply_to_address, String reply_to_name, String feedback, String rating) {
        //File Path NOT Required

        btnSubmit.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(FeedBackActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);

        try {

            final String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(context, uploadId, AppConfig.SERVER_URL + "submit_feedback.php")
//                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("server_url", AppConfig.SERVER_URL) //send serverUrl dynamically
                    .addParameter("user_id", user_id)
                    .addParameter("date_created", date_created)
                    .addParameter("subject", subject)
                    .addParameter("reply_to_address", reply_to_address)
                    .addParameter("reply_to_name", reply_to_name)
                    .addParameter("feedback", feedback)
                    .addParameter("rating", rating)

//                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(0)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {
                            // your code here

                            progressDialog.setMessage("Submitting ...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();

                        }

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse,
                                            Exception exception) {
                            progressDialog.dismiss();
                            btnSubmit.setEnabled(true);
                            Toast.makeText(context, "Server Response : " + serverResponse, Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                            // your code here
                            // if you have mapped your server response to a POJO, you can easily get it:
                            // YourClass obj = new Gson().fromJson(serverResponse.getBodyAsString(), YourClass.class);

                            progressDialog.dismiss();
                            btnSubmit.setEnabled(true);
                            Toast.makeText(getApplicationContext(), "We have successfully received your feedback.", Toast.LENGTH_LONG).show();


                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {
                            // your code here
                        }
                    })
                    .startUpload(); //Starting the upload


        } catch (Exception exc) {
            Toast.makeText(context, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


}
