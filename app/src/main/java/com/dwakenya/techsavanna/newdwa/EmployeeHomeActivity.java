package com.dwakenya.techsavanna.newdwa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dwakenya.techsavanna.newdwa.app.AppConfig;
import com.dwakenya.techsavanna.newdwa.app.AppController;
import com.dwakenya.techsavanna.newdwa.helpers.CircleTransform;
import com.dwakenya.techsavanna.newdwa.helpers.GPSService;
import com.dwakenya.techsavanna.newdwa.helpers.SessionManager;
import com.dwakenya.techsavanna.newdwa.holders.SpecialityHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeHomeActivity extends AppCompatActivity implements View.OnClickListener{

    // ALWAYS TRYING TO UPDATE FCM ID JUST IN CASE ANOTHER ONE IS PROVIDED
    //ID IS ATTACHED TO LOGGED USERID

    String regId="";

    private String TAG = EmployeeHomeActivity.class.getSimpleName();

    private Spinner spinnerSpeciality, spinnerPreferenceSkill;
    private EditText input_where_learnt, input_training_level;
    private Button btn_add_speciality;
    private FloatingActionButton fabSubmitPreference;
    private ToggleButton tbStatus;
    private ImageView ivProfile;
    private TextView tvPreference;
    private TextView tvName;
    private TextView tvLocName;
    private TextView tvCoordinates;

    ArrayList<SpecialityHolder> specialityHolderArrayList;
    ArrayList<SpecialityHolder> prefenceSkillHolderArrayList;

    String selected_speciality,selected_speciality_id, selected_preference,selected_preference_id;



    // Session Manager Class
    SessionManager session;

    HashMap<String, String> user;
    String status;
    String newStatus;

    private String selected_reg = "",  selected_region_id = "", lat = "", lon="", loc="";
    GPSService mGPSService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Home");

        // Session Manager
        session = new SessionManager(getApplicationContext());

        // get user data from session
        user = session.getUserDetails();

        SharedPreferences pref = getApplicationContext().getSharedPreferences(AppConfig.SHARED_PREF, 0);
        regId= pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId)) {

            updateUserToken ();


        }else {

            Toast.makeText(getApplicationContext(), "Firebase Reg Id is not received yet!", Toast.LENGTH_LONG).show();
        }



        spinnerSpeciality = (Spinner) findViewById(R.id.sp_speciality);
        spinnerPreferenceSkill = (Spinner) findViewById(R.id.sp_preference_skill);
        input_where_learnt = (EditText) findViewById(R.id.input_where_learnt);
        input_training_level = (EditText) findViewById(R.id.input_level_of_training);
        btn_add_speciality = (Button) findViewById(R.id.addSpeciality);
        btn_add_speciality.setOnClickListener(this);
        fabSubmitPreference = (FloatingActionButton) findViewById(R.id.fab_update_preference);
        fabSubmitPreference.setOnClickListener(this);
        tbStatus = findViewById(R.id.tb_status);
        tvPreference = findViewById(R.id.user_profile_short_bio);

        tvPreference.setText(user.get(SessionManager.KEY_PREFERENCE));

        tvName = findViewById(R.id.user_profile_name);

        tvName.setText(user.get(SessionManager.KEY_NAME));

        ivProfile = findViewById(R.id.user_profile_photo);

        tvLocName = findViewById(R.id.tv_loc);
        tvCoordinates = findViewById(R.id.tv_coordinates);

        if ((!user.get(SessionManager.KEY_IMAGE_PATH).equals("")) || user.get(SessionManager.KEY_IMAGE_PATH) != null) {
            Glide.with(EmployeeHomeActivity.this).load(user.get(SessionManager.KEY_IMAGE_PATH))
                    .crossFade()
                   // .thumbnail(0.5f)
                     .bitmapTransform(new CircleTransform(EmployeeHomeActivity.this))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(ivProfile);
       }

        tbStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(EmployeeHomeActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                builder.setTitle("Status Change");
                builder.setMessage("Current Status is " + status + "\n" + "Proceed to change status?");

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        if(tbStatus.isChecked())
                        {
                            updateStatus("0");
                        } else {
                            updateStatus("1");
                        }

                    }
                });

                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (user.get(SessionManager.KEY_STATUS).equals("1")) {
                                    status = "Occupied";
                                    tbStatus.setChecked(false);
                                } else if (user.get(SessionManager.KEY_STATUS).equals("0")) {
                                    status = "Available";
                                    tbStatus.setChecked(true);

                                }
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();


            }
        });

        tbStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {

//                AlertDialog.Builder builder = new AlertDialog.Builder(EmployeeHomeActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
//                builder.setTitle("Status Change");
//                builder.setMessage("Current Status is " + status + "\n" + "Proceed to change status?");
//
//                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//
//                        if(isChecked)
//                        {
//                            updateStatus("0");
//                        } else {
//                            updateStatus("1");
//                        }
//
//
//                    }
//                });
//
//                builder.setNegativeButton("Cancel",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                if (user.get(SessionManager.KEY_STATUS).equals("1")) {
//                                    status = "Occupied";
//                                    tbStatus.setChecked(false);
//                                } else if (user.get(SessionManager.KEY_STATUS).equals("0")) {
//                                    status = "Available";
//                                    tbStatus.setChecked(true);
//
//                                }
//                                dialog.dismiss();
//                            }
//                        });
//                AlertDialog alert = builder.create();
//                alert.show();


            }
        });

        specialityHolderArrayList = new ArrayList<>();
        prefenceSkillHolderArrayList = new ArrayList<>();



        fillSpeciality();


        spinnerSpeciality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selected_speciality_id = specialityHolderArrayList.get(position).getId();
                selected_speciality = specialityHolderArrayList.get(position).getSpeciality_name();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        spinnerPreferenceSkill.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selected_preference_id = prefenceSkillHolderArrayList.get(position).getId();
                selected_preference = prefenceSkillHolderArrayList.get(position).getSpeciality_name();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        }

    @Override
    protected void onResume() {
        super.onResume();

        getGPS(EmployeeHomeActivity.this);

        if (user.get(SessionManager.KEY_STATUS).equals("1")) {
            status = "Occupied";
            tbStatus.setChecked(false);
        } else if (user.get(SessionManager.KEY_STATUS).equals("0")) {
            status = "Available";
            tbStatus.setChecked(true);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {


            case R.id.action_logout:

                session.logoutUser();

                break;

            case R.id.action_feedback:

                Intent i2= new Intent(this, FeedBackActivity.class);
                i2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i2);

                break;

            case R.id.action_refresh:

                refresh();

                break;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

        return true;
    }

    public void refresh (){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void fillSpeciality() {
        Thread thread = new Thread() {
            Handler handler = new Handler();
            ProgressDialog progressDialog;
            boolean error;
            @Override
            public void run() {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = new ProgressDialog(EmployeeHomeActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                        progressDialog.setMessage("Loading...");
                        progressDialog.show();
                    }
                });



                final List<String> list = new ArrayList<String>();
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                specialityHolderArrayList.clear();
                adapter.notifyDataSetChanged();


                final List<String> list2 = new ArrayList<String>();
                final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, list2);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                prefenceSkillHolderArrayList.clear();
                adapter2.notifyDataSetChanged();


                HashMap<String, String> params = new HashMap<String, String>();
                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
                        AppConfig.SERVER_URL + "fill_speciality.php", new JSONObject(params),
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
                                        // iterate over array users as declared in php script
                                        JSONArray jArray = object.getJSONArray("specialities");
                                        Log.d(TAG, "Array: " + jArray.toString());

                                        for (int i = 0; i < jArray.length(); i++) {
                                            JSONObject singleRow = jArray.getJSONObject(i);
                                            SpecialityHolder specialityHolder = new SpecialityHolder(
                                                    singleRow.getString("id"), singleRow.getString("speciality_name"));
                                            specialityHolderArrayList.add(specialityHolder);
                                            prefenceSkillHolderArrayList.add(specialityHolder);
                                        }
                                        for (int i = 0; i < specialityHolderArrayList.size(); i++) {
                                            list.add(specialityHolderArrayList.get(i).getSpeciality_name());
                                            // list.add(locationList.get(i).getId());
                                        }


                                        spinnerSpeciality.setAdapter(adapter);

                                        for (int i = 0; i < prefenceSkillHolderArrayList.size(); i++) {
                                            list2.add(prefenceSkillHolderArrayList.get(i).getSpeciality_name());
                                            // list.add(locationList.get(i).getId());
                                        }
                                        spinnerPreferenceSkill.setAdapter(adapter2);

                                    } else {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Equipment available", Toast.LENGTH_LONG).show();
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
    public void onClick(View v) {
        if (v.equals(btn_add_speciality)) {
            if (input_where_learnt.getText().equals("")) {

                Toast.makeText(getApplicationContext(), "Where learnt?", Toast.LENGTH_LONG).show();
            } else {
                addSpeciality();


            }
        }

        if (v.equals(fabSubmitPreference)) {
            updatePreference();
        }
    }

    private void updatePreference () {
        Thread thread = new Thread() {
            ProgressDialog progressDialog;
            Handler handler = new Handler();
            boolean error;

            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = new ProgressDialog(EmployeeHomeActivity.this,
                                R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("Submitting Preference...");
                        progressDialog.show();
                    }
                });


                HashMap<String, String> params = new HashMap<String, String>();

                params.put("user_id", user.get(SessionManager.KEY_USER_ID));
                params.put("speciality_id", selected_preference_id);
                params.put("preference_skill", selected_preference);


                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
                        AppConfig.SERVER_URL + "update_preference.php", new JSONObject(params),
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
                                        Toast.makeText(getBaseContext(), "Preference added successfully", Toast.LENGTH_LONG).show();

                                        input_where_learnt.setText("");


                                    } else {
                                        Toast.makeText(getBaseContext(), "Failed : " + object.getString("error_msg"), Toast.LENGTH_LONG).show();
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

    private void updateStatus (final String newStatus) {
        Thread thread = new Thread() {
            ProgressDialog progressDialog;
            Handler handler = new Handler();
            boolean error;

            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = new ProgressDialog(EmployeeHomeActivity.this,
                                R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("Updating Status...");
                        progressDialog.show();
                    }
                });


                HashMap<String, String> params = new HashMap<String, String>();

                params.put("user_id", user.get(SessionManager.KEY_USER_ID));
                params.put("status", newStatus);


                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
                        AppConfig.SERVER_URL + "update_status.php", new JSONObject(params),
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
                                        Toast.makeText(getBaseContext(), "success", Toast.LENGTH_LONG).show();
                                        refresh();


                                    } else {
                                        Toast.makeText(getBaseContext(), "Failed : " + object.getString("error_msg"), Toast.LENGTH_LONG).show();
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

    private void addSpeciality () {
        Thread thread = new Thread() {
            ProgressDialog progressDialog;
            Handler handler = new Handler();
            boolean error;

            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = new ProgressDialog(EmployeeHomeActivity.this,
                                R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("Submitting...");
                        progressDialog.show();
                    }
                });


                HashMap<String, String> params = new HashMap<String, String>();

                params.put("user_id", user.get(SessionManager.KEY_USER_ID));
                params.put("email1", user.get(SessionManager.KEY_EMAIL));
                params.put("skill", selected_speciality);
                params.put("speciality_id", selected_speciality_id);
                params.put("where_learnt", input_where_learnt.getText().toString());
                params.put("training_level", input_training_level.getText().toString());

                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
                        AppConfig.SERVER_URL + "add_employee_speciality.php", new JSONObject(params),
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
                                        Toast.makeText(getBaseContext(), "Speciality added successfully", Toast.LENGTH_LONG).show();

                                        input_where_learnt.setText("");


                                    } else {
                                        Toast.makeText(getBaseContext(), "Failed : " + object.getString("error_msg"), Toast.LENGTH_LONG).show();
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

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(AppConfig.SHARED_PREF, 0);
        regId= pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId)) {

            Toast.makeText(getApplicationContext(), "Firebase Reg Id: " + regId, Toast.LENGTH_LONG).show();
        }else {

            Toast.makeText(getApplicationContext(), "Firebase Reg Id is not received yet!", Toast.LENGTH_LONG).show();
        }
    }

    private void updateUserToken () {



        Thread thread = new Thread() {

            boolean error;

            @Override
            public void run() {


                HashMap<String, String> params = new HashMap<String, String>();


                params.put("user_id", user.get(SessionManager.KEY_USER_ID));
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


    private void getGPS(Context context) {

        mGPSService = new GPSService(context);
        mGPSService.getLocation();

//            availableLatLngArrayList = new ArrayList<>();

        if (!mGPSService.isLocationAvailable) {

            // Here you can ask the user to try again, using return; for that
            Toast.makeText(context, "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
//            return;

            // Or you can continue without getting the location, remove the return; above and uncomment the line given below
            // address = "Location not available";
        } else {

            // Getting location co-ordinates
            double latitude = mGPSService.getLatitude();
            double longitude = mGPSService.getLongitude();
            String address = mGPSService.getLocationAddress();

            lat = String.valueOf(latitude);
            lon = String.valueOf(longitude);

            if (address.contains("IO Exception") || address.contains("Error") || address.contains("not found")) {
                loc = "";
            } else {
                loc = address;
            }

            tvCoordinates.setText(lat+","+lon);
            tvLocName.setText(loc);

        }

    }
}
