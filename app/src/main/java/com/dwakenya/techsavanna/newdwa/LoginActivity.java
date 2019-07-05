package com.dwakenya.techsavanna.newdwa;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dwakenya.techsavanna.newdwa.app.AppConfig;
import com.dwakenya.techsavanna.newdwa.app.AppController;
import com.dwakenya.techsavanna.newdwa.helpers.NotificationUtils;
import com.dwakenya.techsavanna.newdwa.helpers.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_NETWORK_STATE,
            android.Manifest.permission.READ_PHONE_STATE

    };
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String regId="";

    Button _loginButton;
    TextView _signupLink;

    private TextInputLayout inputLayoutEmail, inputLayoutPassword;
    private EditText _emailText, _passwordText;

    // Session Manager Class
    SessionManager session;

    private static final int STORAGE_PERMISSION_CODE = 123;

    String msg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _loginButton = findViewById(R.id.btn_login);
        _signupLink = findViewById(R.id.link_signup);

        _emailText = (EditText) findViewById(R.id.input_email);
        _passwordText = (EditText) findViewById(R.id.input_password);


        session = new SessionManager(getApplicationContext());



//        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
//                // checking for type intent filter
//                if (intent.getAction().equals(AppConfig.REGISTRATION_COMPLETE)) {
//                    // gcm successfully registered
//                    // now subscribe to `global` topic to receive app wide notifications
//                    FirebaseMessaging.getInstance().subscribeToTopic(AppConfig.TOPIC_GLOBAL);
//
//                    SharedPreferences pref = getApplicationContext().getSharedPreferences(AppConfig.SHARED_PREF, 0);
//                    regId = pref.getString("regId", null);
//
//                    displayFirebaseRegId();
//
//                    if (!TextUtils.isEmpty(regId)){
//                        updateUserToken();
//                    }
//
//
//
//                } else if (intent.getAction().equals(AppConfig.PUSH_NOTIFICATION)) {
//                    // new push notification is received
//
//                    String message = intent.getStringExtra("message");
//
//                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
//
////                    txtMessage.setText(message);
//                }
//            }
//        };

//        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
//        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);




        _signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                finish();

            }
        });


        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//
//                Intent intent = new Intent(LoginActivity.this, SearchActivity.class);
//                startActivity(intent);
//                finish();


                login();



            }
        });


    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        // register GCM registration complete receiver
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                new IntentFilter(AppConfig.REGISTRATION_COMPLETE));
//
//        // register new push message receiver
//        // by doing this, the activity will be notified each time a new message arrives
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                new IntentFilter(AppConfig.PUSH_NOTIFICATION));
//
//        // clear the notification area when the app is opened
//        NotificationUtils.clearNotifications(getApplicationContext());
//    }
//
//    @Override
//    protected void onPause() {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
//        super.onPause();
//    }


    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            return;
        }

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        loginFxn(email, password);
    }


    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(AppConfig.SHARED_PREF, 0);
        regId= pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId)) {

            //Toast.makeText(getApplicationContext(), "Firebase Reg Id: " + regId, Toast.LENGTH_LONG).show();
        }else {

            //Toast.makeText(getApplicationContext(), "Firebase Reg Id is not received yet!", Toast.LENGTH_LONG).show();
        }
    }




    @Override
    public void onBackPressed() {

        super.onBackPressed();

        //disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public boolean validate() {
        boolean valid = true;
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }
        if (password.isEmpty() || password.length() < 1 || password.length() > 10) {
            _passwordText.setError("between 1 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }
        return valid;
    }

    private void loginFxn(final String email, final String password) {

        Thread thread = new Thread() {
            Handler handler = new Handler();
            String username = "";
            String phone = "";
            ProgressDialog progressDialog;
            boolean error;

            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = new ProgressDialog(LoginActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                        progressDialog.setMessage("Authenticating...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                    }
                });

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
                        AppConfig.SERVER_URL+"login.php", new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(final JSONObject response) {
                                progressDialog.hide();
                                Log.d(TAG, " On Response: " + response.toString());
                                try {
                                    // Parse
                                    JSONArray jArray = response.getJSONArray("post");
                                    JSONObject er = jArray.getJSONObject(0);
                                    error = er.getBoolean("error");

                                    if (!error) {
                                        // user successfully logged in
//
                                        JSONObject user = jArray.getJSONObject(1);
                                        JSONObject user1 = user.getJSONObject("user");
                                        String uid = user1.getString("id");
                                        String name = user1.getString("first_name") + " "+ user1.getString("last_name");
                                        System.out.println("name:"+name);
                                        String phone = user1.getString("phone_cell");
                                        String email = user1.getString("email1");
                                        String status = user1.getString("status");
                                        String user_type = user1.getString("user_type");

                                        String server_url = user1.getString("server_url");
                                        String image_url = user1.getString("image_url");
                                        String preference_skill = user1.getString("preference_skill");
                                        String image_path = server_url.trim()+image_url.trim();

                                        System.out.println("uid ns"+uid);
                                        System.out.println("phone ns"+phone);
                                        System.out.println("status ns"+status);
                                        System.out.println("user_type ns"+user_type);
                                        System.out.println("preference_skill ns"+preference_skill);
                                        System.out.println("image_path ns"+image_path);

                                        session.createLoginSession(name, email, uid, phone, status, user_type,preference_skill, image_path);
                                        // Not Sure is the

                                        if (user_type.equals("1")) {
                                            //Employee
                                            Intent intent = new Intent(LoginActivity.this,SearchActivity.class);
                                            intent.putExtra("type",user_type);
                                            intent.putExtra("phone", phone);
                                            startActivity(intent);
                                            finish();


                                        } else if (user_type.equals("0")) {
                                            //Employer
                                            Intent intent = new Intent(LoginActivity.this, SearchActivity.class);
                                            intent.putExtra("type", user_type);
                                            intent.putExtra("phone", phone);
                                            startActivity(intent);
                                            finish();


                                        } else {
                                            Toast.makeText(getApplicationContext(), "User Not Defined!", Toast.LENGTH_LONG).show();
                                        }





//                                        Intent i = new Intent(LoginActivity.this, BasicDetailsActivity.class);
//                                        startActivity(i);
//                                        finish();

                                    } else {

                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {

                                                Toast.makeText(getApplicationContext(), "Login credentials failed! Please try again", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }

                                    VolleyLog.v("Response:%n %s", response.toString(4));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
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

    private void updateUserToken () {



        Thread thread = new Thread() {

            boolean error;

            @Override
            public void run() {


                HashMap<String, String> params = new HashMap<String, String>();


                params.put("user_id", "10");
                params.put("fcm_token", regId);


                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
                        AppConfig.SERVER_URL + "fcm_registration.php", new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(final JSONObject response) {
                                Log.d(TAG, " On Response: " + response.toString());
                                try {

                                    // Json parsing from response
                                    JSONObject object = new JSONObject(response.toString());

                                    // check error
                                    error = object.getBoolean("error");
                                    if (!error) {

                                    } else {

                                    }
                                    VolleyLog.v("Response:%n %s", response.toString(4));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
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

    @Override
    protected void onResume() {
        super.onResume();

        final SharedPreferences pref = getApplicationContext().getSharedPreferences(AppConfig.SHARED_PREF, 0);
        msg= pref.getString("msg", null);

        if (!TextUtils.isEmpty(msg)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
            builder.setTitle("Last Notification");
            builder.setMessage(msg);

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("Don't Show Again",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = pref.edit();
                            editor.remove("msg");
                            editor.apply();
                            dialog.dismiss();
                        }
                    });


            AlertDialog alert = builder.create();
            alert.show();



        }

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    //Requesting permission
    private void requestPermissions() {
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);

        if (
                ActivityCompat.checkSelfPermission(LoginActivity.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(LoginActivity.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(LoginActivity.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(LoginActivity.this, permissionsRequired[3]) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(LoginActivity.this, permissionsRequired[4]) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(LoginActivity.this, permissionsRequired[5]) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(LoginActivity.this, permissionsRequired[6]) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(LoginActivity.this, permissionsRequired[7]) != PackageManager.PERMISSION_GRANTED) {
            if (
                    ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, permissionsRequired[0])
                            || ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, permissionsRequired[1])
                            || ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, permissionsRequired[2])
                            || ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, permissionsRequired[3])
                            || ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, permissionsRequired[4])
                            || ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, permissionsRequired[5])
                            || ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, permissionsRequired[6])
                            || ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, permissionsRequired[7])
            ) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("Get One requires these permissions for normal functionality.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(LoginActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Permissions Required");
                builder.setMessage("Get One requires these permissions for normal functionality.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Click on Permissions and Grant Access", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(LoginActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0], true);
            editor.apply();
        } else {

            //You already have the permission, just go ahead.
//            proceedAfterPermission();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                proceedAfterPermission();
            } else if (
                    ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, permissionsRequired[0])
                            || ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, permissionsRequired[1])
                            || ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, permissionsRequired[2])
                            || ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, permissionsRequired[3])
                            || ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, permissionsRequired[4])
                            || ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, permissionsRequired[5])
                            || ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, permissionsRequired[6])
                            || ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, permissionsRequired[7])

            ) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("Get One requires these permissions for normal functionality.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(LoginActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(LoginActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
//                proceedAfterPermission();
            }
        }
    }

    private void proceedAfterPermission() {

        Toast.makeText(getBaseContext(), "Thank You For Accepting Required Permissions", Toast.LENGTH_LONG).show();

        _passwordText.setEnabled(true);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(LoginActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission

//                _passwordText.setEnabled(true);
                proceedAfterPermission();
            }
        }
    }
}
